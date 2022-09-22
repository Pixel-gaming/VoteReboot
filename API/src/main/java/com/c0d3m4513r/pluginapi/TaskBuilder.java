package com.c0d3m4513r.pluginapi;

import lombok.NonNull;

import java.util.concurrent.TimeUnit;

public interface TaskBuilder {
    @NonNull
    TaskBuilder async(boolean async);
    @NonNull
    TaskBuilder deferred(long timeAmount,@NonNull TimeUnit timeValue);
    @NonNull
    TaskBuilder timer(long timeAmount, @NonNull TimeUnit timeValue);
    @NonNull
    TaskBuilder executer(@NonNull Runnable run);
    @NonNull
    TaskBuilder name(@Nullable String name);
    @NonNull
    TaskBuilder reset();
    @NonNull
    Task build() throws IllegalArgumentException;

}
