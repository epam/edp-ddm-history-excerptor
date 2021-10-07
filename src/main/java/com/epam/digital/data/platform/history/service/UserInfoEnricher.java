package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.client.DigitalSignatureRestClient;
import com.epam.digital.data.platform.dso.client.exception.BadRequestException;
import com.epam.digital.data.platform.dso.client.exception.InternalServerErrorException;
import com.epam.digital.data.platform.dso.client.exception.SignatureValidationException;
import com.epam.digital.data.platform.history.model.HistoryExcerptData;
import com.epam.digital.data.platform.history.model.UserInfo;
import com.epam.digital.data.platform.integration.ceph.dto.FormDataDto;
import com.epam.digital.data.platform.integration.ceph.service.CephService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserInfoEnricher {

  private static final UserInfo EMPTY_USER_INFO = new UserInfo();
  private static final String SIGNATURE = "signature";
  private static final String DATA = "data";

  private final Logger log = LoggerFactory.getLogger(UserInfoEnricher.class);

  private final String historicSignatureBucket;
  private final ObjectMapper objectMapper;
  private final DigitalSignatureRestClient digitalSignatureRestClient;
  private final CephService historicSignatureCephService;

  public UserInfoEnricher(
      @Value("${historic-signature-ceph.bucket}") String historicSignatureBucket,
      ObjectMapper objectMapper,
      DigitalSignatureRestClient digitalSignatureRestClient,
      CephService historicSignatureCephService) {
    this.historicSignatureBucket = historicSignatureBucket;
    this.objectMapper = objectMapper;
    this.digitalSignatureRestClient = digitalSignatureRestClient;
    this.historicSignatureCephService = historicSignatureCephService;
  }

  public void enrichWithUserInfo(HistoryExcerptData historyData) {
    for (var row : historyData.getExcerptRows()) {
      var cephKey = row.getDdmInfo().getDigitalSign();
      row.setUserInfo(getUserInfo(cephKey));
    }
  }

  private UserInfo getUserInfo(String key) {
    if (key == null) {
      log.error("Signature not saved. Key == null");
      return EMPTY_USER_INFO;
    }
    var content = historicSignatureCephService.getContent(historicSignatureBucket, key);
    if (content.isEmpty()) {
      log.error("Signature not found for key {}", key);
      return EMPTY_USER_INFO;
    }

    String signature;
    String data;
    try {
      var formDataDto = objectMapper.readValue(content.get(), FormDataDto.class);
      data = objectMapper.writeValueAsString(formDataDto.getData());
      signature = formDataDto.getSignature();
    } catch (JsonProcessingException e) {
      throw new RuntimeJsonMappingException(e.getMessage());
    }

    OwnerResponseDto owner;
    try {
      owner = digitalSignatureRestClient.getOwnerInfinite(
          new VerificationRequestDto(signature, data));
    } catch (BadRequestException e) {
      log.error("Bad request. {}", e.getErrorDto().getMessage());
      return EMPTY_USER_INFO;
    } catch (InternalServerErrorException e) {
      log.error("Internal server error. {}", e.getErrorDto().getMessage());
      return EMPTY_USER_INFO;
    } catch (SignatureValidationException e) {
      log.error("Signature validation error. {}", e.getErrorDto().getMessage());
      return EMPTY_USER_INFO;
    }

    if (owner == null) {
      log.error("Failed to get the owner of the digital signature");
      return EMPTY_USER_INFO;
    }

    return new UserInfo(owner.getFullName(), owner.getDrfo(), owner.getEdrpou());
  }
}
