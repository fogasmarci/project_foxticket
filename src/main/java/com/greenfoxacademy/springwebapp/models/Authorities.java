package com.greenfoxacademy.springwebapp.models;

public enum Authorities {
  USER,
  ADMIN;

  @Override
  public String toString() {
    return String.format("ROLE_%s", name());
  }
}
