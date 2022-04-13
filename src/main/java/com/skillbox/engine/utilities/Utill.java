package com.skillbox.engine.utilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class Utill {
    public static LocalDateTime convertLongToLocalDateTime(Long longTime) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(longTime),
                TimeZone.getDefault().toZoneId());
        if (time.compareTo(LocalDateTime.now())<1){
            time = LocalDateTime.now();
        }
        return time;
    }
}
