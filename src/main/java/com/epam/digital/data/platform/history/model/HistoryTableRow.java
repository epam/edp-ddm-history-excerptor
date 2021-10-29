package com.epam.digital.data.platform.history.model;

import java.util.Map;

public class HistoryTableRow {
  private HistoryTableRowDdmInfo ddmInfo;
  private Map<String, OperationalTableField> operationalTableData;

  public HistoryTableRow() {
  }

  public HistoryTableRow(
      HistoryTableRowDdmInfo ddmInfo,
      Map<String, OperationalTableField> operationalTableData) {
    this.ddmInfo = ddmInfo;
    this.operationalTableData = operationalTableData;
  }

  public HistoryTableRowDdmInfo getDdmInfo() {
    return ddmInfo;
  }

  public void setDdmInfo(HistoryTableRowDdmInfo ddmInfo) {
    this.ddmInfo = ddmInfo;
  }

  public Map<String, OperationalTableField> getOperationalTableData() {
    return operationalTableData;
  }

  public void setOperationalTableData(Map<String, OperationalTableField> operationalTableData) {
    this.operationalTableData = operationalTableData;
  }
}
