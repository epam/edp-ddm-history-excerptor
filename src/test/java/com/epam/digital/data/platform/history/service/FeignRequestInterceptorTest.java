package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.history.util.ThirdPartyHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FeignRequestInterceptorTest {

  private static final String TOKEN = "token";

  private final RequestInterceptor requestInterceptor = new FeignRequestInterceptor(TOKEN);

  @Test
  void expectAccessTokenHeaderAddedToRequest() {
    var requestTemplate = new RequestTemplate();

    requestInterceptor.apply(requestTemplate);

    assertThat(requestTemplate.headers())
        .hasSize(1)
        .containsExactly(
            Map.entry(
                ThirdPartyHeader.ACCESS_TOKEN.getHeaderName(), Collections.singletonList(TOKEN)));
  }
}
