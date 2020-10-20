package com.rccl.demo.web.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date withZeroUTC(LocalDateTime localDateTime) {
        return toDate(localDateTime, ZoneId.of("GMT"));
    }

    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
