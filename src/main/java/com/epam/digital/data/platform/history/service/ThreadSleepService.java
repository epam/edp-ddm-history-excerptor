package com.epam.digital.data.platform.history.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ThreadSleepService {

  public void sleep(long timeoutInSeconds) throws InterruptedException {
    TimeUnit.SECONDS.sleep(timeoutInSeconds);
  }
}
