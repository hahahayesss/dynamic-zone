package com.r00t.dynamiczone.util;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;

import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ZoneUtils {
    private static final String NETMERA_PACKAGE = "com.r00t.dynamiczone.model";

    public static final Map<String, BiFunction<Object, ZoneId, Object>> CONVERTER = new HashMap<>() {{
        put("long",
            (o, zoneId) -> toLocalTimestamp((long) o, zoneId));

        put(Long.class.getName(),
            (o, zoneId) -> toLocalTimestamp((long) o, zoneId));

        put(Date.class.getName(),
            (o, zoneId) -> toLocalDate((Date) o, zoneId));
    }};

    // ================================================================================================================

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static LocalDateTime nowDateTime() {
        return now().toLocalDateTime();
    }

    public static LocalDate nowDate() {
        return now().toLocalDate();
    }

    public static LocalTime nowTime() {
        return now().toLocalTime();
    }

    // ================================================================================================================

    public static void convertFields(Object object, ZoneId zoneId) {
        if (object instanceof ResponseEntity<?>) {
            convertFields(((ResponseEntity<?>) object).getBody(), zoneId);

        } else if (object instanceof Collection<?>) {
            convertFields((Collection<?>) object, zoneId);

        } else if (object instanceof Map<?, ?>) {
            convertFields((Map<?, ?>) object, zoneId);

        } else if (object instanceof Object[]) {
            convertFields((Object[]) object, zoneId);

        } else if (object.getClass().getPackageName().startsWith(NETMERA_PACKAGE)) {
            convertPojoFields(object, zoneId);

        } else {
            throw new UnsupportedOperationException("Object type not supported");
        }
    }

    public static void convertFields(Map<?, ?> keyAndObjects, ZoneId zoneId) {
        for (Object key : keyAndObjects.keySet())
            convertFields(
                    keyAndObjects.get(key),
                    zoneId
            );
    }

    public static void convertFields(Collection<?> objects, ZoneId zoneId) {
        convertFields(
                objects.toArray(Object[]::new),
                zoneId
        );
    }

    public static void convertFields(Object[] objects, ZoneId zoneId) {
        for (Object obj : objects)
            convertPojoFields(obj, zoneId);
    }

    public static void convertPojoFields(Object object, ZoneId zoneId) {
        ReflectionUtils.doWithFields(
                object.getClass(),
                field -> {
                    field.setAccessible(true);
                    String fieldTypeName = field.getType().getTypeName();
                    if (CONVERTER.containsKey(fieldTypeName))
                        field.set(
                                object,
                                CONVERTER.get(fieldTypeName)
                                         .apply(field.get(object), zoneId)
                        );
                },
                field -> field.isAnnotationPresent(Zoned.class)
        );

        ReflectionUtils.doWithFields(
                object.getClass(),
                field -> {
                    field.setAccessible(true);
                    convertFields(field.get(object), zoneId);
                },
                field -> field.getType().getPackageName().startsWith(NETMERA_PACKAGE)
        );
    }

    // ================================================================================================================

    public static Object convertParameter(Object object, ZoneId zoneId) {
        if (object instanceof Long) {
            return toLocalTimestamp((long) object, zoneId);

        } else if (object instanceof Date date) {
            return toLocalDate((Date) object, zoneId);
        }

        throw new UnsupportedOperationException("Parameter type not supported");
    }

    // ================================================================================================================

    public static ZonedDateTime toZonedDateTime(long timestamp, ZoneId timestampZone) {
        return Instant.ofEpochMilli(timestamp)
                      .atZone(timestampZone)
                      .withZoneSameLocal(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(long timestamp, String timestampZoneId) {
        return Instant.ofEpochMilli(timestamp)
                      .atZone(ZoneId.of(timestampZoneId))
                      .withZoneSameLocal(ZoneId.systemDefault());
    }

    // ================================================================================================================

    public static Instant toLocalInstant(long timestamp, ZoneId timestampZone) {
        return toZonedDateTime(timestamp, timestampZone)
                .toInstant();
    }

    public static Instant toLocalInstant(long timestamp, String timestampZoneId) {
        return toZonedDateTime(timestamp, timestampZoneId)
                .toInstant();
    }

    // ================================================================================================================

    public static long toLocalTimestamp(long timestamp, ZoneId timestampZone) {
        return toLocalInstant(timestamp, timestampZone)
                .toEpochMilli();
    }

    public static long toLocalTimestamp(long timestamp, String timestampZoneId) {
        return toLocalInstant(timestamp, timestampZoneId)
                .toEpochMilli();
    }

    // ================================================================================================================

    public static LocalDateTime toLocalDateTime(long timestamp, ZoneId timestampZone) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                timestampZone
        );
    }

    public static LocalDateTime toLocalDateTime(long timestamp, String timestampZoneId) {
        return toLocalDateTime(
                timestamp,
                ZoneId.of(timestampZoneId)
        );
    }

    // ================================================================================================================

    public static Date toLocalDate(Date date, ZoneId dateZone) {
        return new Date(
                toLocalInstant(
                        date.toInstant().toEpochMilli(),
                        dateZone
                ).toEpochMilli()
        );
    }

    public static Date toLocalDate(Date date, String dateZoneId) {
        return toLocalDate(date, ZoneId.of(dateZoneId));
    }
}
