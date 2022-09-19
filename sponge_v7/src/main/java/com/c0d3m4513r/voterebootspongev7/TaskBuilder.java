package com.c0d3m4513r.voterebootspongev7;

import com.c0d3m4513r.voterebootapi.Task;
import lombok.NonNull;
import org.spongepowered.api.Sponge;

import java.util.concurrent.TimeUnit;

public class TaskBuilder implements com.c0d3m4513r.voterebootapi.TaskBuilder {
    private boolean async = false;
    private boolean timer = false;
    private long timerTimeAmount = 0;
    private TimeUnit timerTimeValue = null;
    private boolean deferred = false;
    private long deferredTimeAmount = 0;
    private TimeUnit deferredTimeValue = null;
    private Object cntxt = null;
    private Runnable run = null;
    private static Object plugin;

    TaskBuilder(Object plugint){
        TaskBuilder.plugin=plugint;
    }

    @Override
    public TaskBuilder async(boolean async) {
        this.async=async;
        return this;
    }

    @Override
    public TaskBuilder deferred(long timeAmount, TimeUnit timeValue) {
        if (timeAmount!=0){
            deferred=true;
            deferredTimeAmount=timeAmount;
            deferredTimeValue=timeValue;
        }else{
            deferred=false;
            deferredTimeAmount=timeAmount;
            deferredTimeValue=null;
        }
        return this;
    }

    @Override
    public TaskBuilder timer(long timeAmount, TimeUnit timeValue) {
        if (timeAmount!=0){
            timer=true;
            timerTimeAmount=timeAmount;
            timerTimeValue=timeValue;
        }else{
            timer=false;
            timerTimeAmount=timeAmount;
            timerTimeValue=null;
        }
        return this;
    }


    @Override
    public TaskBuilder executer(Runnable run) {
        this.run=run;
        return this;
    }

    @Override
    public com.c0d3m4513r.voterebootapi.TaskBuilder reset() {
        async = false;
        timer = false;
        timerTimeAmount = 0;
        timerTimeValue = null;
        deferred = false;
        deferredTimeAmount = 0;
        deferredTimeValue = null;
        cntxt = null;
        run = null;
        return this;
    }

    @Override
    @NonNull
    public Task build() throws IllegalArgumentException{
        org.spongepowered.api.scheduler.Task.Builder builder1 = Sponge.getScheduler().createTaskBuilder();
        if (this.async) builder1=builder1.async();
        if (this.deferred) builder1=builder1.delay(deferredTimeAmount,deferredTimeValue);
        if (this.timer) builder1=builder1.interval(timerTimeAmount,timerTimeValue);
        if (this.run==null || this.cntxt == null) throw new IllegalArgumentException("Expected to have a non-null Runnable and context");
        return (Task) builder1.execute(this.run).submit(this.cntxt);
    }
}