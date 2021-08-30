package com.epam.digital.data.platform.history.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "schema")
public class SchemaProperties {

  private String name;
  private String historyTableSuffix;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHistoryTableSuffix() {
    return historyTableSuffix;
  }

  public void setHistoryTableSuffix(String historyTableSuffix) {
    this.historyTableSuffix = historyTableSuffix;
  }
}
