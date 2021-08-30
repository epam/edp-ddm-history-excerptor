package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.excerpt.model.ExcerptEntityId;
import com.epam.digital.data.platform.excerpt.model.ExcerptEventDto;
import com.epam.digital.data.platform.excerpt.model.ExcerptProcessingStatus;
import com.epam.digital.data.platform.excerpt.model.StatusDto;
import com.epam.digital.data.platform.history.exception.HistoryExcerptGenerationException;
import com.epam.digital.data.platform.history.util.ExcerptHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcerptServiceTest {

  private static final String EXCEPRT_ACCESS_TOKEN = "token";
  private static final int EXCERPT_STATUS_CHECK_MAX_ATTEMPTS = 2;
  private static final UUID EXCERPT_ID = UUID.fromString("f45f4010-bc09-4c98-8a8b-1aad4fa5a19f");

  private static final String SIGNATURE = "sign";
  private static final String CEPH_KEY = "key";

  private ExcerptService excerptService;

  @Mock
  private ExcerptRestClient excerptRestClient;
  @Mock
  private DigitalSignatureService digitalSignatureService;
  @Mock
  private ThreadSleepService threadSleepService;

  @Captor
  private ArgumentCaptor<Map<String, Object>> requestHeadersCaptor;

  @BeforeEach
  void beforeEach() {
    excerptService =
        new ExcerptService(
            EXCEPRT_ACCESS_TOKEN,
            EXCERPT_STATUS_CHECK_MAX_ATTEMPTS,
            excerptRestClient,
            digitalSignatureService,
            threadSleepService);
  }

  @Test
  void expectValidRequestToRestClient() {
    when(excerptRestClient.generate(any(), any()))
            .thenReturn(new ExcerptEntityId(EXCERPT_ID));
    when(digitalSignatureService.sign(any()))
            .thenReturn(SIGNATURE);
    when(digitalSignatureService.saveSignature(SIGNATURE))
            .thenReturn(CEPH_KEY);

    var eventDto = new ExcerptEventDto();

    var actual = excerptService.generate(eventDto);

    verify(excerptRestClient).generate(eq(eventDto), requestHeadersCaptor.capture());
    var actualHeaders = (Map<String, Object>) requestHeadersCaptor.getValue();
    assertThat(actualHeaders)
            .hasSize(3)
            .containsEntry(ExcerptHeader.ACCESS_TOKEN.getHeaderName(), EXCEPRT_ACCESS_TOKEN)
            .containsEntry(ExcerptHeader.X_DIGITAL_SIGNATURE.getHeaderName(), CEPH_KEY)
            .containsEntry(ExcerptHeader.X_DIGITAL_SIGNATURE_DERIVED.getHeaderName(), CEPH_KEY);

    assertThat(actual).isEqualTo(EXCERPT_ID);
  }

  @Test
  void expectStatusReturnedWhenNotInProgress() throws InterruptedException {
    var expectedStatus = new StatusDto(ExcerptProcessingStatus.COMPLETED, "");
    when(excerptRestClient.status(
            EXCERPT_ID, Map.of(ExcerptHeader.ACCESS_TOKEN.getHeaderName(), EXCEPRT_ACCESS_TOKEN)))
        .thenReturn(expectedStatus);

    var actual = excerptService.getFinalProcessingStatus(EXCERPT_ID);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedStatus);
  }

  @Test
  void expectTimeoutUntilNotInProgress() throws InterruptedException {
    var expectedStatus = new StatusDto(ExcerptProcessingStatus.FAILED, "");

    when(excerptRestClient.status(
            EXCERPT_ID, Map.of(ExcerptHeader.ACCESS_TOKEN.getHeaderName(), EXCEPRT_ACCESS_TOKEN)))
            .thenReturn(new StatusDto(ExcerptProcessingStatus.IN_PROGRESS, ""))
            .thenReturn(expectedStatus);

    var actual = excerptService.getFinalProcessingStatus(EXCERPT_ID);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expectedStatus);

    verify(threadSleepService).sleep(5);
  }

  @Test
  void expectExceptionIfStatusCheckExceedsMaxAttempts() {
    var inProgressStatus = new StatusDto(ExcerptProcessingStatus.IN_PROGRESS, "");
    when(excerptRestClient.status(
            EXCERPT_ID, Map.of(ExcerptHeader.ACCESS_TOKEN.getHeaderName(), EXCEPRT_ACCESS_TOKEN)))
            .thenReturn(inProgressStatus);

    assertThrows(HistoryExcerptGenerationException.class, () -> excerptService.getFinalProcessingStatus(EXCERPT_ID));
  }
}
