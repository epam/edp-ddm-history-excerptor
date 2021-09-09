package com.epam.digital.data.platform.history.util;

import com.epam.digital.data.platform.history.model.OperationalTableField;
import com.epam.digital.data.platform.history.model.OperationalTableFieldType;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class OperationalTableFieldResolvingUtil {

  private static final Map<Class<?>, Function<Object, OperationalTableField>>
          OPERATIONAL_TABLE_FIELD_TYPE_MAP =
          Map.of(
                  Timestamp.class,
                  column -> {
                    var dateTime = DateUtils.getDateTimeFromSqlTimestamp((Timestamp) column);
                    return new OperationalTableField(
                            dateTime.toString(), OperationalTableFieldType.DATETIME);
                  },
                  Date.class,
                  column -> {
                    var date = DateUtils.getDateFromSql((Date) column);
                    return new OperationalTableField(date.toString(), OperationalTableFieldType.DATE);
                  },
                  Time.class,
                  column -> {
                    var time = DateUtils.getTimeFromSql((Time) column);
                    return new OperationalTableField(time.toString(), OperationalTableFieldType.TIME);
                  });

  private OperationalTableFieldResolvingUtil() {}

  public static OperationalTableField resolveOperationalFieldValue(Object sqlColumnValue) {
    return OPERATIONAL_TABLE_FIELD_TYPE_MAP.entrySet().stream()
        .filter(entry -> entry.getKey().isInstance(sqlColumnValue))
        .findFirst()
        .map(entry -> entry.getValue().apply(sqlColumnValue))
        .orElse(
            new OperationalTableField(
                Optional.ofNullable(sqlColumnValue).map(Object::toString).orElse(null),
                OperationalTableFieldType.TEXT));
  }
}
