package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.history.config.properties.OpenshiftProperties;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Component;

@Component
public class OpenShiftService {

  private final OpenshiftProperties openshiftProperties;
  private final KubernetesClient kubernetesClient;

  public OpenShiftService(
          OpenshiftProperties openshiftProperties, KubernetesClient kubernetesClient) {
    this.openshiftProperties = openshiftProperties;
    this.kubernetesClient = kubernetesClient;
  }

  public void updateParentJobWithResultValue(String result) {
    if (openshiftProperties.isEnabled()) {
      kubernetesClient
          .batch()
          .jobs()
          .withName(openshiftProperties.getParentJob().getName())
          .edit()
          .editMetadata()
          .addToAnnotations(openshiftProperties.getParentJob().getResultField(), result)
          .endMetadata()
          .done();
    }
  }
}
