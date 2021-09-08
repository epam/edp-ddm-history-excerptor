package com.epam.digital.data.platform.history.model;

import java.util.Objects;

public class UserInfo {

  private String fullName;
  private String drfo;
  private String edrpou;

  public UserInfo() {
  }

  public UserInfo(String fullName, String drfo, String edrpou) {
    this.fullName = fullName;
    this.drfo = drfo;
    this.edrpou = edrpou;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getDrfo() {
    return drfo;
  }

  public void setDrfo(String drfo) {
    this.drfo = drfo;
  }

  public String getEdrpou() {
    return edrpou;
  }

  public void setEdrpou(String edrpou) {
    this.edrpou = edrpou;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(fullName, userInfo.fullName) && Objects.equals(drfo,
        userInfo.drfo) && Objects.equals(edrpou, userInfo.edrpou);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName, drfo, edrpou);
  }
}
