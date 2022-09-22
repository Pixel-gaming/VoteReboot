package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.Nullable;
import lombok.*;

import java.util.concurrent.TimeUnit;

public class ManualAction extends RestartAction {
    public String reason;
    private ManualAction(){
        super(RestartType.Manual);
        reason=null;
    }
    private ManualAction(@Nullable String reason) {
        this();
        this.reason = reason;
    }
    public ManualAction(@Nullable String reason, long timer, @NonNull TimeUnit unit){
        this(reason);
        this.timer.set(timer);
        this.timerUnit.set(unit);
    }

}
