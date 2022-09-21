package com.c0d3m4513r.pluginapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeUnitValue implements Comparable<TimeUnitValue> {
    @NonNull
    TimeUnit unit;
    @NonNull
    long value;

    void add(TimeUnitValue unitValue){
        value+=unit.convert(unitValue.value,unitValue.unit);
    }

    @Override
    public int compareTo(TimeUnitValue o) {
        //todo:review this. There is a potentially better way
        long ov = unit.convert(o.value,o.unit);
        if (value-ov>0){
            return 1;
        }else if (value-ov<0){
            return -1;
        }else {
            return 0;
        }
    }
}
