package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.dso.api.dto.SignRequestDto;
import com.epam.digital.data.platform.dso.api.dto.SignResponseDto;
import com.epam.digital.data.platform.dso.client.DigitalSealRestClient;
import com.epam.digital.data.platform.excerpt.model.ExcerptEventDto;
import com.epam.digital.data.platform.integration.ceph.service.CephService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigitalSignatureServiceTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String LOWCODE_BUCKET = "bucket";
  private static final String SIGNATURE = "signature";
  private static final String DATA = "test_data";

  private DigitalSignatureService digitalSignatureService;

  @Mock
  private DigitalSealRestClient digitalSealRestClient;
  @Mock
  private CephService requestSignatureCephService;

  @BeforeEach
  void beforeEach() {
    digitalSignatureService =
        new DigitalSignatureService(
            LOWCODE_BUCKET, OBJECT_MAPPER, digitalSealRestClient, requestSignatureCephService);
  }

  @Test
  void expectSignedRequest() {
    var excerptEvent = new ExcerptEventDto();
    var body =
        "{\"recordId\":null,\"excerptType\":null,\"excerptInputData\":null,\"requiresSystemSignature\":false}";
    var signRequestDto = new SignRequestDto(body);

    SignResponseDto signResponseDto = new SignResponseDto();
    signResponseDto.setSignature(SIGNATURE);
    when(digitalSealRestClient.sign(signRequestDto)).thenReturn(signResponseDto);

    String actual = digitalSignatureService.sign(excerptEvent);

    assertThat(actual).isEqualTo(String.format("{\"signature\":\"%s\"}", SIGNATURE));
  }

  @Test
  void expectSignatureSavedWithCeph() {
    digitalSignatureService.saveSignature(DATA);

    verify(requestSignatureCephService).putContent(eq(LOWCODE_BUCKET), any(), eq(DATA));
  }
}
