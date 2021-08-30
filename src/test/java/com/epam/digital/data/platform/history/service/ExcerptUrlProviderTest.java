package com.epam.digital.data.platform.history.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ExcerptUrlProviderTest {

  private static final String EXCERPT_URL = "http://url";

  private ExcerptUrlProvider excerptUrlProvider;

  @BeforeEach
  void beforeEach() {
    excerptUrlProvider = new ExcerptUrlProvider(EXCERPT_URL);
  }

  @Test
  void expectValidRetrieveUrlBuilt() {
    var id = UUID.fromString("3cc262c1-0cd8-4d45-be66-eb0fca821e0a");

    var actual = excerptUrlProvider.getRetrieveExcerptUrl(id);

    assertThat(actual).isEqualTo(EXCERPT_URL + "/excerpts/" + id.toString());
  }
}
