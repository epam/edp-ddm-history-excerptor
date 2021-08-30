package com.epam.digital.data.platform.history;

import com.epam.digital.data.platform.history.service.HistoryExcerptCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class HistoryExcerptorApplication implements ApplicationRunner {

  private final Logger log = LoggerFactory.getLogger(HistoryExcerptorApplication.class);

  private final HistoryExcerptCreationService historyExcerptCreationService;

  public HistoryExcerptorApplication(HistoryExcerptCreationService historyExcerptCreationService) {
    this.historyExcerptCreationService = historyExcerptCreationService;
  }

  public static void main(String[] args) {
    SpringApplication.run(HistoryExcerptorApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var tableName = getTableName(args);
    var entityId = getEntityId(args);
    log.info("Start excerpt creation flow with tableName {}, id {}", tableName, entityId);
    historyExcerptCreationService.createExcerpt(tableName, entityId);
  }

  private String getTableName(ApplicationArguments args) {
    return Optional.ofNullable(args.getOptionValues("tableName"))
        .filter(table -> table.size() == 1)
        .map(table -> table.get(0))
        .orElseThrow(() -> new IllegalArgumentException("Invalid table name specification"));
  }

  private UUID getEntityId(ApplicationArguments args) {
    return Optional.ofNullable(args.getOptionValues("id"))
            .filter(id -> id.size() == 1)
            .map(id -> id.get(0))
            .map(this::getUUIDFromIdString)
            .orElseThrow(() -> new IllegalArgumentException("Invalid entity id specification"));
  }

  private UUID getUUIDFromIdString(String id) {
    try {
      return UUID.fromString(id);
    } catch (Exception e) {
      throw new IllegalArgumentException(
              String.format("UUID expected in entityId, but have: %s", id));
    }
  }
}
