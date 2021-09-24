package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.history.config.properties.OpenshiftProperties;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class OpenShiftService {

  private final OpenshiftProperties openshiftProperties;
  private final Supplier<KubernetesClient> kubernetesClientFactory;

  public OpenShiftService(
      OpenshiftProperties openshiftProperties, Supplier<KubernetesClient> kubernetesClientFactory) {
    this.openshiftProperties = openshiftProperties;
    this.kubernetesClientFactory = kubernetesClientFactory;
  }

  public void updateParentJobWithResultValue(String result) {
    if (openshiftProperties.isEnabled()) {
      KubernetesClient kubernetesClient = null;
      try {
        kubernetesClient = kubernetesClientFactory.get();
        kubernetesClient
                .batch()
                .jobs()
                .withName(openshiftProperties.getParentJob().getName())
                .edit()
                .editMetadata()
                .addToAnnotations(openshiftProperties.getParentJob().getResultField(), result)
                .endMetadata()
                .done();
      } finally {
        if (kubernetesClient != null) {
          kubernetesClient.close();
        }
      }
    }
  }
}
