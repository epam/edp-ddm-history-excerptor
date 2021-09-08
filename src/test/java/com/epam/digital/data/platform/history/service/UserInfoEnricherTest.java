package com.epam.digital.data.platform.history.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.client.DigitalSignatureRestClient;
import com.epam.digital.data.platform.dso.client.exception.BadRequestException;
import com.epam.digital.data.platform.dso.client.exception.InternalServerErrorException;
import com.epam.digital.data.platform.dso.client.exception.SignatureValidationException;
import com.epam.digital.data.platform.history.model.HistoryExcerptData;
import com.epam.digital.data.platform.history.model.HistoryExcerptRow;
import com.epam.digital.data.platform.history.model.HistoryExcerptRowDdmInfo;
import com.epam.digital.data.platform.history.model.UserInfo;
import com.epam.digital.data.platform.integration.ceph.service.CephService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserInfoEnricherTest {

  private static final UserInfo EMPTY_USER_INFO = new UserInfo();
  private static final ErrorDto ERROR_DTO = new ErrorDto("code", "msg", "local_msg");
  private static final String HISTORIC_BUCKET = "bucket";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String DATA = "\"test_data\"";
  private static final String SIGNATURE = "test_signature";
  private static final String CEPH_KEY = "ceph_key";
  private static final String CEPH_VALUE =
      "{\"data\":" + DATA + ",\"signature\":\"" + SIGNATURE + "\"}";

  private UserInfoEnricher userInfoEnricher;
  private HistoryExcerptData historyExcerptData;

  @Mock
  private DigitalSignatureRestClient digitalSignatureRestClient;
  @Mock
  private CephService historicSignatureCephService;

  @BeforeEach
  void beforeEach() {
    userInfoEnricher = new UserInfoEnricher(HISTORIC_BUCKET, OBJECT_MAPPER,
        digitalSignatureRestClient, historicSignatureCephService);

    HistoryExcerptRowDdmInfo ddmInfo = new HistoryExcerptRowDdmInfo();
    ddmInfo.setDigitalSign(CEPH_KEY);

    historyExcerptData = new HistoryExcerptData(null,
        List.of(new HistoryExcerptRow(ddmInfo, null)));

    when(historicSignatureCephService.getContent(HISTORIC_BUCKET, CEPH_KEY))
        .thenReturn(Optional.of(CEPH_VALUE));
  }

  @Test
  void shouldEnrichWithEmptyUserInfoWhenThereIsNoFileInCephBucket() {
    when(historicSignatureCephService.getContent(HISTORIC_BUCKET, CEPH_KEY))
        .thenReturn(Optional.empty());
    assertEmptyUserInfo();
  }

  @Test
  void shouldEnrichWithEmptyUserInfoWhenInvalidSignature() {
    assertEmptyUserInfo();
  }

  @Test
  void shouldEnrichWithCorrectUserInfo() {
    when(digitalSignatureRestClient.getOwnerInfinite(new VerificationRequestDto(SIGNATURE, DATA)))
        .thenReturn(new OwnerResponseDto("name", "drfo", "edrpou"));

    userInfoEnricher.enrichWithUserInfo(historyExcerptData);

    var userInfo = historyExcerptData.getExcerptRows().get(0).getUserInfo();
    assertThat(userInfo).isEqualTo(new UserInfo("name", "drfo", "edrpou"));
  }

  @Test
  void shouldReturnEmptyUserInfoWhenBadRequestException() {
    when(digitalSignatureRestClient.getOwnerInfinite(any()))
        .thenThrow(new BadRequestException(ERROR_DTO));
    assertEmptyUserInfo();
  }

  @Test
  void shouldReturnEmptyUserInfoWhenInternalServerErrorException() {
    when(digitalSignatureRestClient.getOwnerInfinite(any()))
        .thenThrow(new InternalServerErrorException(ERROR_DTO));
    assertEmptyUserInfo();
  }

  @Test
  void shouldReturnEmptyUserInfoWhenSignatureValidationException() {
    when(digitalSignatureRestClient.getOwnerInfinite(any()))
        .thenThrow(new SignatureValidationException(ERROR_DTO));
    assertEmptyUserInfo();
  }

  private void assertEmptyUserInfo() {

    userInfoEnricher.enrichWithUserInfo(historyExcerptData);

    var userInfo = historyExcerptData.getExcerptRows().get(0).getUserInfo();
    assertThat(userInfo).isEqualTo(EMPTY_USER_INFO);
  }
}