package com.epam.digital.data.platform.history.config;

import com.epam.digital.data.platform.history.config.properties.OpenshiftProperties;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenShiftConfiguration {

  @Bean
  public KubernetesClient openShiftClient(OpenshiftProperties openshiftProperties) {
    return new DefaultOpenShiftClient().inNamespace(openshiftProperties.getNamespace());
  }
}
