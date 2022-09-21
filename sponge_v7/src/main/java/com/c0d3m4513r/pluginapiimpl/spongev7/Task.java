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
    public boolean cancel() {
        return task.cancel();
    }
}
