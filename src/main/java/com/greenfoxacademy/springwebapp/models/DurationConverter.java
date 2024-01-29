package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {
  @Override
  public String convertToDatabaseColumn(Duration duration) {
    if (duration.toDays() > 1) {
      return duration.toDays() + " days";
    } else if (duration.toHours() > 1) {
      return duration.toHours() + " hours";
    } else if (duration.toMinutes() >= 1) {
      return duration.toMinutes() + " minutes";
    }
    return null;
  }

  @Override
  public Duration convertToEntityAttribute(String dbData) {
    String[] data = dbData.split(" ");
    if (data[1].contains("days")) {
      return Duration.ofDays(Integer.parseInt(data[0]));
    } else if (data[1].contains("hours")) {
      return Duration.ofHours(Integer.parseInt(data[0]));
    } else if (data[1].contains("min")) {
      return Duration.ofMinutes(Integer.parseInt(data[0]));
    }
    return Duration.ZERO;
  }
}
