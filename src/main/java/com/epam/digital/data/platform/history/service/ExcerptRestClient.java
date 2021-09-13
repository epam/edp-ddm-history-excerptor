package com.epam.digital.data.platform.history.service;

import com.epam.digital.data.platform.excerpt.model.ExcerptEntityId;
import com.epam.digital.data.platform.excerpt.model.ExcerptEventDto;
import com.epam.digital.data.platform.excerpt.model.StatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "excerpt-client", url = "${excerpt.url}", path = "/excerpts")
public interface ExcerptRestClient {

  @PostMapping
  ExcerptEntityId generate(
      @RequestBody ExcerptEventDto excerptEventDto,
      @RequestHeader Map<String, Object> headers);

  @GetMapping("/{id}/status")
  StatusDto status(@PathVariable("id") UUID id);
}
