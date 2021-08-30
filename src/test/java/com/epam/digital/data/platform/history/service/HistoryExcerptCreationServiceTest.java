package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.excerpt.model.ExcerptEventDto;
import com.epam.digital.data.platform.excerpt.model.ExcerptProcessingStatus;
import com.epam.digital.data.platform.excerpt.model.StatusDto;
import com.epam.digital.data.platform.history.exception.HistoryExcerptGenerationException;
import com.epam.digital.data.platform.history.model.HistoryExcerptData;
import com.epam.digital.data.platform.history.repository.HistoryDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.EXCERPT_TYPE;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_DATA_FIELD;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_ENTITY_ID_FIELD;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_TABLE_NAME_FIELD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryExcerptCreationServiceTest {

  private static final String TABLE_NAME = "table";
  private static final UUID ENTITY_ID = UUID.fromString("3cc262c1-0cd8-4d45-be66-eb0fca821e0a");

  private static final UUID EXCERPT_ID = UUID.fromString("f45f4010-bc09-4c98-8a8b-1aad4fa5a19f");
  private static final String EXCERPT_RETRIEVE_URL = "http://excerpt";

  private HistoryExcerptCreationService historyExcerptCreationService;

  @Mock
  private HistoryDataRepository historyDataRepository;
  @Mock
  private ExcerptService excerptService;
  @Mock
  private ExcerptUrlProvider excerptUrlProvider;
  @Mock
  private OpenShiftService openShiftService;

  private final HistoryExcerptData mockData = new HistoryExcerptData();

  @BeforeEach
  void beforeEach() throws InterruptedException {
    when(historyDataRepository.getHistoryData(TABLE_NAME, ENTITY_ID))
            .thenReturn(mockData);
    when(excerptService.generate(any()))
            .thenReturn(EXCERPT_ID);
    when(excerptService.getFinalProcessingStatus(EXCERPT_ID))
        .thenReturn(new StatusDto(ExcerptProcessingStatus.COMPLETED, ""));

    historyExcerptCreationService =
        new HistoryExcerptCreationService(
                historyDataRepository, excerptService, excerptUrlProvider, openShiftService);
  }

  @Test
  void expectExcerptGenerationFromValidEvent() throws InterruptedException {
    historyExcerptCreationService.createExcerpt(TABLE_NAME, ENTITY_ID);

    var excerptEventCaptor = ArgumentCaptor.forClass(ExcerptEventDto.class);
    verify(excerptService).generate(excerptEventCaptor.capture());

    var actualExcerptEvent = excerptEventCaptor.getValue();

    var expectedExcerptEvent = new ExcerptEventDto();
    expectedExcerptEvent.setExcerptType(EXCERPT_TYPE);
    expectedExcerptEvent.setExcerptInputData(Map.of(
            INPUT_TABLE_NAME_FIELD, TABLE_NAME,
            INPUT_ENTITY_ID_FIELD, ENTITY_ID,
            INPUT_DATA_FIELD, mockData
    ));
    expectedExcerptEvent.setRequiresSystemSignature(false);

    assertThat(actualExcerptEvent).usingRecursiveComparison().isEqualTo(expectedExcerptEvent);
  }

  @Test
  void expectCallToOpenshiftWhenExcerptCompleted() throws InterruptedException {
    when(excerptService.getFinalProcessingStatus(EXCERPT_ID))
            .thenReturn(new StatusDto(ExcerptProcessingStatus.COMPLETED, ""));
    when(excerptUrlProvider.getRetrieveExcerptUrl(EXCERPT_ID))
            .thenReturn(EXCERPT_RETRIEVE_URL);

    historyExcerptCreationService.createExcerpt(TABLE_NAME, ENTITY_ID);

    verify(openShiftService).updateParentJobWithResultValue(EXCERPT_RETRIEVE_URL);
  }

  @Test
  void expectExceptionWhenExcerptFailure() throws InterruptedException {
    var errorStatusDetails = "error";
    when(excerptService.getFinalProcessingStatus(EXCERPT_ID))
            .thenReturn(new StatusDto(ExcerptProcessingStatus.FAILED, errorStatusDetails));

    var exception = assertThrows(
        HistoryExcerptGenerationException.class,
        () -> historyExcerptCreationService.createExcerpt(TABLE_NAME, ENTITY_ID));

    assertThat(exception.getMessage()).isEqualTo(errorStatusDetails);
  }
}
