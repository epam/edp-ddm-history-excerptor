package com.epam.digital.data.platform.history.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

public class DdmSourceSystem {
  @Id
  private UUID systemId;
  private String systemName;
  private String createdBy;
  private LocalDateTime createdAt;

  public UUID getSystemId() {
    return systemId;
  }

  public void setSystemId(UUID systemId) {
    this.systemId = systemId;
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
