package com.epam.digital.data.platform.history.model;

import java.util.Map;

public class HistoryExcerptRow {

  private HistoryExcerptRowDdmInfo ddmInfo;
  private Map<String, OperationalTableField> operationalTableData;
  private UserInfo userInfo;

  public HistoryExcerptRow() {
  }

  public HistoryExcerptRow(
          HistoryExcerptRowDdmInfo ddmInfo, Map<String, OperationalTableField> operationalTableData) {
    this.ddmInfo = ddmInfo;
    this.operationalTableData = operationalTableData;
  }

  public HistoryExcerptRowDdmInfo getDdmInfo() {
    return ddmInfo;
  }

  public void setDdmInfo(HistoryExcerptRowDdmInfo ddmInfo) {
    this.ddmInfo = ddmInfo;
  }

  public Map<String, OperationalTableField> getOperationalTableData() {
    return operationalTableData;
  }

  public void setOperationalTableData(Map<String, OperationalTableField> operationalTableData) {
    this.operationalTableData = operationalTableData;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }
}
