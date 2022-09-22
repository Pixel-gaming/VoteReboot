package com.c0d3m4513r.pluginapi.config;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.convert.Convert;
import lombok.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Data
@Setter(AccessLevel.NONE)
@Getter(AccessLevel.NONE)
@NoArgsConstructor
public class TimeEntry{
    public long days;
    public long hours;
    public long minutes;
    public long seconds;
    public long ms;
    public long us;
    public long ns;

    public static Optional<TimeEntry> of(String parse){
        TimeEntry te = new TimeEntry();
        for(val s:parse.split("\\+")){
            try{
                String[] tv = s.split(",");
                TimeUnit u = Convert.asTimeUnit(tv[1]).get();
                switch (u){
                    case DAYS:te.days+=Long.parseLong(tv[0]); break;
                    case HOURS:te.hours+=Long.parseLong(tv[0]); break;
                    case MINUTES:te.minutes+=Long.parseLong(tv[0]); break;
                    case SECONDS:te.seconds+=Long.parseLong(tv[0]); break;
                    case MILLISECONDS:te.ms+=Long.parseLong(tv[0]); break;
                    case MICROSECONDS:te.us+=Long.parseLong(tv[0]); break;
                    case NANOSECONDS:te.ns+=Long.parseLong(tv[0]); break;
                }
            }catch (ArrayIndexOutOfBoundsException | NumberFormatException | NoSuchElementException e){
                API.getLogger().error("[API] Could not convert string to TimeEntry. Error is: ",e);
                return Optional.empty();
            }
        }
        return Optional.of(te);
    }

    public void add(TimeEntry te){
        days+=te.days;
        hours+=te.hours;
        minutes+=te.minutes;
        seconds+=te.seconds;
        ms+=te.ms;
        us+=te.us;
        ns+=te.ns;
    }
    public long getHours(){return days*24+hours;}
    public long getMinutes(){return getHours()*60+minutes;}
    public long getSeconds(){return getMinutes()*60+seconds;}
    public long getMs(){return getSeconds()*1000+ms;}
    public long getUs(){return getMs()*1000+us;}
    public long getNs(){return getUs()*1000+ns;}
    public TimeUnitValue getMaxUnit(){
        if (ns==0){
            if (us==0){
                if (ms==0){
                    if (seconds==0){
                        if (minutes==0){
                            if (hours==0){
                                return new TimeUnitValue(TimeUnit.DAYS,days);
                            }else{
                                return new TimeUnitValue(TimeUnit.HOURS,getHours());
                            }
                        }else{
                            return new TimeUnitValue(TimeUnit.MINUTES,getMinutes());
                        }
                    }else{
                        return new TimeUnitValue(TimeUnit.SECONDS,getSeconds());
                    }
                }else{
                    return new TimeUnitValue(TimeUnit.MILLISECONDS,getMs());
                }
            }else{
                return new TimeUnitValue(TimeUnit.MICROSECONDS,getUs());
            }
        }else{
            return new TimeUnitValue(TimeUnit.NANOSECONDS,getNs());
        }
    }
    public boolean equals(long value,TimeUnit unit){
        switch (unit){
            case NANOSECONDS:return value==getNs();
            case MICROSECONDS:return value==getUs();
            case MILLISECONDS:return value==getMs();
            case SECONDS:return value==getSeconds();
            case MINUTES:return value==getMinutes();
            case HOURS:return value==getHours();
            case DAYS:return value==days;
            default:throw new RuntimeException("TimeUnit had more Enum variants than expected");
        }
    }

}
