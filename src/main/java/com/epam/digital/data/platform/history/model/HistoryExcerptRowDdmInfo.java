package com.epam.digital.data.platform.history.model;

public class HistoryExcerptRowDdmInfo {
  private String createdAt;
  private String createdBy;
  private String dmlOp;
  private String systemId;
  private String applicationId;
  private String businessProcessId;
  private String businessProcessDefinitionId;
  private String businessProcessInstanceId;
  private String businessActivity;
  private String businessActivityInstanceId;
  private String digitalSign;
  private String digitalSignDerived;
  private String digitalSignChecksum;
  private String digitalSignDerivedChecksum;

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

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getBusinessProcessId() {
    return businessProcessId;
  }

  public void setBusinessProcessId(String businessProcessId) {
    this.businessProcessId = businessProcessId;
  }

  public String getBusinessProcessDefinitionId() {
    return businessProcessDefinitionId;
  }

  public void setBusinessProcessDefinitionId(String businessProcessDefinitionId) {
    this.businessProcessDefinitionId = businessProcessDefinitionId;
  }

  public String getBusinessProcessInstanceId() {
    return businessProcessInstanceId;
  }

  public void setBusinessProcessInstanceId(String businessProcessInstanceId) {
    this.businessProcessInstanceId = businessProcessInstanceId;
  }

  public String getBusinessActivity() {
    return businessActivity;
  }

  public void setBusinessActivity(String businessActivity) {
    this.businessActivity = businessActivity;
  }

  public String getBusinessActivityInstanceId() {
    return businessActivityInstanceId;
  }

  public void setBusinessActivityInstanceId(String businessActivityInstanceId) {
    this.businessActivityInstanceId = businessActivityInstanceId;
  }

  public String getDigitalSign() {
    return digitalSign;
  }

  public void setDigitalSign(String digitalSign) {
    this.digitalSign = digitalSign;
  }

  public String getDigitalSignDerived() {
    return digitalSignDerived;
  }

  public void setDigitalSignDerived(String digitalSignDerived) {
    this.digitalSignDerived = digitalSignDerived;
  }

  public String getDigitalSignChecksum() {
    return digitalSignChecksum;
  }

  public void setDigitalSignChecksum(String digitalSignChecksum) {
    this.digitalSignChecksum = digitalSignChecksum;
  }

  public String getDigitalSignDerivedChecksum() {
    return digitalSignDerivedChecksum;
  }

  public void setDigitalSignDerivedChecksum(String digitalSignDerivedChecksum) {
    this.digitalSignDerivedChecksum = digitalSignDerivedChecksum;
  }
}
