package com.epam.digital.data.platform.history.model;

import java.util.List;

public class HistoryExcerptData {

  private List<String> operationalTableFields;
  private List<HistoryExcerptRow> excerptRows;

  public HistoryExcerptData() {
  }

  public HistoryExcerptData(List<String> operationalTableFields, List<HistoryExcerptRow> excerptRows) {
    this.operationalTableFields = operationalTableFields;
    this.excerptRows = excerptRows;
  }

  public List<String> getOperationalTableFields() {
    return operationalTableFields;
  }

  public void setOperationalTableFields(List<String> operationalTableFields) {
    this.operationalTableFields = operationalTableFields;
  }

  public List<HistoryExcerptRow> getExcerptRows() {
    return excerptRows;
  }

  public void setExcerptRows(List<HistoryExcerptRow> excerptRows) {
    this.excerptRows = excerptRows;
  }
}
