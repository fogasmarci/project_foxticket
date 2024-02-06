package com.greenfoxacademy.springwebapp.models;

import com.greenfoxacademy.springwebapp.exceptions.fields.DurationIsMalformedException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {
  public static final String DAYS = "days";
  public static final String HOURS = "hours";
  public static final String MINUTES = "minutes";

  @Override
  public String convertToDatabaseColumn(Duration duration) {
    if (duration.toDays() > 1) {
      return String.format("%s %s", duration.toDays(), DAYS);
    } else if (duration.toHours() > 1) {
      return String.format("%s %s", duration.toHours(), HOURS);
    } else if (duration.toMinutes() >= 1) {
      return String.format("%s %s", duration.toMinutes(), MINUTES);
    }
    throw new DurationIsMalformedException();
  }

  @Override
  public Duration convertToEntityAttribute(String dbData) {
    return convertDateToDuration(dbData);
  }

  public static Duration convertDateToDuration(String dbData) {
    if (dbData == null) {
      throw new DurationIsMalformedException();
    }

    String[] data = dbData.split(" ");
    switch (data[1]) {
      case DAYS:
        return Duration.ofDays(Integer.parseInt(data[0]));
      case HOURS:
        return Duration.ofHours(Integer.parseInt(data[0]));
      case MINUTES:
        return Duration.ofMinutes(Integer.parseInt(data[0]));
      default:
        throw new DurationIsMalformedException();
    }
  }
}
