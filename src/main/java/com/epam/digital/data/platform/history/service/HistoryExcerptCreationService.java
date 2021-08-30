package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.excerpt.model.ExcerptEventDto;
import com.epam.digital.data.platform.excerpt.model.ExcerptProcessingStatus;
import com.epam.digital.data.platform.excerpt.model.StatusDto;
import com.epam.digital.data.platform.history.exception.HistoryExcerptGenerationException;
import com.epam.digital.data.platform.history.model.HistoryExcerptData;
import com.epam.digital.data.platform.history.repository.HistoryDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.EXCERPT_TYPE;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_DATA_FIELD;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_ENTITY_ID_FIELD;
import static com.epam.digital.data.platform.history.util.HistoryExcerptUtil.INPUT_TABLE_NAME_FIELD;

@Service
public class HistoryExcerptCreationService {

  private final Logger log = LoggerFactory.getLogger(HistoryExcerptCreationService.class);

  private final HistoryDataRepository historyDataRepository;
  private final ExcerptService excerptService;
  private final ExcerptUrlProvider excerptUrlProvider;
  private final OpenShiftService openShiftService;

  public HistoryExcerptCreationService(
      HistoryDataRepository historyDataRepository,
      ExcerptService excerptService,
      ExcerptUrlProvider excerptUrlProvider,
      OpenShiftService openShiftService) {
    this.historyDataRepository = historyDataRepository;
    this.excerptService = excerptService;
    this.excerptUrlProvider = excerptUrlProvider;
    this.openShiftService = openShiftService;
  }

  public void createExcerpt(String tableName, UUID id) throws InterruptedException {
    var historyData = historyDataRepository.getHistoryData(tableName, id);

    var excerptEvent = createExcerptEvent(tableName, id, historyData);
    var excerptId = excerptService.generate(excerptEvent);

    StatusDto excerptStatus = excerptService.getFinalProcessingStatus(excerptId);
    if (ExcerptProcessingStatus.COMPLETED.equals(excerptStatus.getStatus())) {
      var excerptRetrieveUrl = excerptUrlProvider.getRetrieveExcerptUrl(excerptId);
      openShiftService.updateParentJobWithResultValue(excerptRetrieveUrl);
      log.info("Excerpt can be retrieved from {}", excerptRetrieveUrl);
    } else {
      throw new HistoryExcerptGenerationException(excerptStatus.getStatusDetails());
    }
  }

  private ExcerptEventDto createExcerptEvent(
      String tableName, UUID entityId, HistoryExcerptData excerptData) {
    var excerptEvent = new ExcerptEventDto();
    excerptEvent.setExcerptType(EXCERPT_TYPE);
    Map<String, Object> excerptInputData = new HashMap<>();
    excerptInputData.put(INPUT_TABLE_NAME_FIELD, tableName);
    excerptInputData.put(INPUT_ENTITY_ID_FIELD, entityId);
    excerptInputData.put(INPUT_DATA_FIELD, excerptData);
    excerptEvent.setExcerptInputData(excerptInputData);
    excerptEvent.setRequiresSystemSignature(false);
    return excerptEvent;
  }
}
