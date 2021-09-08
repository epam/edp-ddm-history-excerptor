package com.epam.digital.data.platform.history.config;

import com.epam.digital.data.platform.dso.client.DigitalSealRestClient;
import com.epam.digital.data.platform.dso.client.DigitalSignatureRestClient;
import com.epam.digital.data.platform.history.service.ExcerptRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {
    ExcerptRestClient.class,
    DigitalSealRestClient.class,
    DigitalSignatureRestClient.class})
public class RestClientConfig {

}
