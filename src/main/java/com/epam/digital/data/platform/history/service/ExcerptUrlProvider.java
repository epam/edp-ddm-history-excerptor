package com.epam.digital.data.platform.history.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExcerptUrlProvider {

  private static final String EXCERPT_RETRIEVE_URL_PATTERN = "%s/excerpts/%s";

  private final String excerptUrl;

  public ExcerptUrlProvider(@Value("${excerpt.url}") String excerptUrl) {
    this.excerptUrl = excerptUrl;
  }

  public String getRetrieveExcerptUrl(UUID id) {
    return String.format(EXCERPT_RETRIEVE_URL_PATTERN, excerptUrl, id);
  }
}
