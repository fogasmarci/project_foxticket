package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {
  public static final String days = "days";
  public static final String hours = "hours";
  public static final String minutes = "minutes";

  @Override
  public String convertToDatabaseColumn(Duration duration) {
    if (duration.toDays() > 1) {
      return String.format("%s %s", duration.toDays(), days);
    } else if (duration.toHours() > 1) {
      return String.format("%s %s", duration.toHours(), hours);
    } else if (duration.toMinutes() >= 1) {
      return String.format("%s %s", duration.toMinutes(), minutes);
    }
    return null;
  }

  @Override
  public Duration convertToEntityAttribute(String dbData) {
    String[] data = dbData.split(" ");
    switch (data[1]) {
      case days:
        return Duration.ofDays(Integer.parseInt(data[0]));
      case hours:
        return Duration.ofHours(Integer.parseInt(data[0]));
      case minutes:
        return Duration.ofMinutes(Integer.parseInt(data[0]));
      default:
        return Duration.ZERO;
    }
  }
}
