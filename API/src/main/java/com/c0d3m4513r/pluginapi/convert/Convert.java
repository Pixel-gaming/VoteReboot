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

    /**
     * @param unit
     * @param bound
     * @return next lower TimeUnit from unit, but with lower bound of bound
     */
    public static TimeUnit nextLowerUnitBounded(TimeUnit unit, TimeUnit bound){
        TimeUnit lower = nextLowerUnit(unit);
        return isLower(bound,lower)?lower:bound;
    }

    public static TimeUnit nextLowerUnit(TimeUnit unit){
        switch (unit){
            case NANOSECONDS:return TimeUnit.NANOSECONDS;
            case MICROSECONDS:return TimeUnit.NANOSECONDS;
            case MILLISECONDS:return TimeUnit.MICROSECONDS;
            case SECONDS:return TimeUnit.MILLISECONDS;
            case MINUTES:return TimeUnit.SECONDS;
            case HOURS:return TimeUnit.MINUTES;
            case DAYS:return TimeUnit.HOURS;
            default:return unit;
        }
    }

    public static boolean isLower(TimeUnit u1, TimeUnit u2){
        switch (u1){
            case NANOSECONDS:
                switch (u2){
                    case NANOSECONDS:
                        return false;
                    default:
                        return true;
                }
            case MICROSECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                        return false;
                    default:
                        return true;
                }
            case MILLISECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                        return false;
                    default:
                        return true;
                }
            case SECONDS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                        return false;
                    default:
                        return true;
                }
            case MINUTES:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                        return false;
                    default:
                        return true;
                }
            case HOURS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                    case HOURS:
                        return false;
                    default:
                        return true;
                }
            case DAYS:
                switch (u2){
                    case NANOSECONDS:
                    case MICROSECONDS:
                    case MILLISECONDS:
                    case SECONDS:
                    case MINUTES:
                    case HOURS:
                    case DAYS:
                        return false;
                    default:
                        return true;
                }
            default:
                return false;
        }
    }
}
