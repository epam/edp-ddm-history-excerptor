package com.epam.digital.data.platform.history.model;

public class HistoryExcerptRowDdmInfo {
  private String createdAt;
  private String createdBy;
  private String dmlOp;
  private String system;
  private String application;
  private String businessProcessId;
  private String businessActivity;

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getDmlOp() {
    return dmlOp;
  }

  public void setDmlOp(String dmlOp) {
    this.dmlOp = dmlOp;
  }

  public String getSystem() {
    return system;
  }

  public void setSystem(String system) {
    this.system = system;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public String getBusinessProcessId() {
    return businessProcessId;
  }

  public void setBusinessProcessId(String businessProcessId) {
    this.businessProcessId = businessProcessId;
  }

  public String getBusinessActivity() {
    return businessActivity;
  }

  public void setBusinessActivity(String businessActivity) {
    this.businessActivity = businessActivity;
  }
}
