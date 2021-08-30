package com.epam.digital.data.platform.history.model;

import java.util.Map;

public class HistoryExcerptRow {
  private HistoryExcerptRowDdmInfo ddmInfo;
  private Map<String, String> operationalTableData;

  public HistoryExcerptRow() {
  }

  public HistoryExcerptRow(
          HistoryExcerptRowDdmInfo ddmInfo, Map<String, String> operationalTableData) {
    this.ddmInfo = ddmInfo;
    this.operationalTableData = operationalTableData;
  }

  public HistoryExcerptRowDdmInfo getDdmInfo() {
    return ddmInfo;
  }

  public void setDdmInfo(HistoryExcerptRowDdmInfo ddmInfo) {
    this.ddmInfo = ddmInfo;
  }

  public Map<String, String> getOperationalTableData() {
    return operationalTableData;
  }

  public void setOperationalTableData(Map<String, String> operationalTableData) {
    this.operationalTableData = operationalTableData;
  }
}
