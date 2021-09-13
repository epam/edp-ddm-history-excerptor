package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.history.util.ThirdPartyHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

  private final String accessToken;

  public FeignRequestInterceptor(@Value("${thirdPartySystems.accessToken}") String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public void apply(RequestTemplate requestTemplate) {
    requestTemplate.header(ThirdPartyHeader.ACCESS_TOKEN.getHeaderName(), accessToken);
  }
}
