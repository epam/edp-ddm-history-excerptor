package com.epam.digital.data.platform.history.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

public final class DateUtils {

  private DateUtils() {}

  public static OffsetDateTime getDateTimeFromSqlTimestamp(Timestamp timestamp) {
    return timestamp.toInstant()
            .atZone(ZoneOffset.UTC)
            .toOffsetDateTime();
  }

  public static LocalDate getDateFromSql(Date date) {
    return date.toLocalDate();
  }

  public static OffsetTime getTimeFromSql(Time time) {
    return new java.util.Date(time.getTime())
            .toInstant()
            .atZone(ZoneOffset.UTC)
            .toOffsetDateTime()
            .toOffsetTime();
  }
}
