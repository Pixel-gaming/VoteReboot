package com.c0d3m4513r.pluginapiimpl.spongev7;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class Task implements com.c0d3m4513r.pluginapi.Task {
    @NonNull
    org.spongepowered.api.scheduler.Task task;


    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public long getDelay() {
        return task.getDelay();
    }

    @Override
    public long getInterval() {
        return task.getInterval();
    }

    @Override
    public boolean isAsynchronous() {
        return task.isAsynchronous();
    }

    /**
     * Cancels the task. Cancelling a repeating task will prevent any further
     * repetitions of the task.
     *
     * @return If the task is not running and was cancelled
     */
    @Override
    public boolean cancel() {
        return task.cancel();
    }
}
