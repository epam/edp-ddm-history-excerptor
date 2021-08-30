package com.epam.digital.data.platform.history.util;

public enum ExcerptHeader {
  ACCESS_TOKEN("X-Access-Token"),
  X_DIGITAL_SIGNATURE("X-Digital-Signature"),
  X_DIGITAL_SIGNATURE_DERIVED("X-Digital-Signature-Derived");

  private final String headerName;

  ExcerptHeader(String headerName) {
    this.headerName = headerName;
  }

  public String getHeaderName() {
    return headerName;
  }
}
