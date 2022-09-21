package com.c0d3m4513r.pluginapi.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Convert {
    private static final Map<String, TimeUnit> timeUnit = new HashMap<String, TimeUnit>(){
        {
            put("s",TimeUnit.SECONDS);
            put("seconds",TimeUnit.SECONDS);
            put("m",TimeUnit.MINUTES);
            put("minute",TimeUnit.MINUTES);
            put("minutes",TimeUnit.MINUTES);
            put("h",TimeUnit.HOURS);
            put("hour",TimeUnit.HOURS);
            put("hours",TimeUnit.HOURS);
            put("d",TimeUnit.DAYS);
            put("day",TimeUnit.DAYS);
            put("days",TimeUnit.DAYS);
        }
    };
    public static Optional<TimeUnit> asTimeUnit(String parse){
        return Optional.ofNullable(timeUnit.get(parse));
    }
}
