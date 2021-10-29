package com.epam.digital.data.platform.history.model;

import java.util.List;

public class HistoryTableData {

  private List<String> operationalTableFields;
  private List<HistoryTableRow> tableRows;

  public HistoryTableData() {}

  public HistoryTableData(List<String> operationalTableFields, List<HistoryTableRow> tableRows) {
    this.operationalTableFields = operationalTableFields;
    this.tableRows = tableRows;
  }

  public List<String> getOperationalTableFields() {
    return operationalTableFields;
  }

  public void setOperationalTableFields(List<String> operationalTableFields) {
    this.operationalTableFields = operationalTableFields;
  }

  public List<HistoryTableRow> getTableRows() {
    return tableRows;
  }

  public void setTableRows(List<HistoryTableRow> tableRows) {
    this.tableRows = tableRows;
  }
}
