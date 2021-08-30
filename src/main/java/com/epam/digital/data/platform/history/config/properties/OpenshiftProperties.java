package com.epam.digital.data.platform.history.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openshift")
public class OpenshiftProperties {

  private boolean enabled;
  private String namespace;
  private JobProperties parentJob;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public JobProperties getParentJob() {
    return parentJob;
  }

  public void setParentJob(JobProperties parentJob) {
    this.parentJob = parentJob;
  }

  public static class JobProperties {

    private String name;
    private String resultField;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getResultField() {
      return resultField;
    }

    public void setResultField(String resultField) {
      this.resultField = resultField;
    }
  }
}
