package com.c0d3m4513r.voterebootapi.reboot;

import com.c0d3m4513r.voterebootapi.Nullable;
import lombok.*;

import java.util.concurrent.TimeUnit;

public class ManualAction extends Action {
    public String reason;
    public ManualAction(){
        super(RestartType.ManualRestart);
        reason=null;
    }
    public ManualAction(@Nullable String reason) {
        this();
        this.reason = reason;
    }
    public ManualAction(@Nullable String reason, long timer, @NonNull TimeUnit unit){
        this(reason);
        this.timer.set(timer);
        this.timerUnit=unit;
    }

}
