package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.c0d3m4513r.pluginapi.API.getLogger;

public class ScheduledAction extends RestartAction implements Runnable{
    EventRegistrar event;
    private ScheduledAction(){
        super(RestartType.Scheduled);
    }
    public ScheduledAction(@NonNull TimeUnitValue tuv){
        this();
        this.timer.set(tuv.getValue());
        this.timerUnit.set(tuv.getUnit());
        event=new EventRegistrar(()->this.intStart(false), EventType.commandRegister,0);
    }
}
