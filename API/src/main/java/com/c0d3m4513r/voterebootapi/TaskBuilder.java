package com.c0d3m4513r.voterebootapi;

import lombok.NonNull;

import java.util.concurrent.TimeUnit;

public interface TaskBuilder {
    TaskBuilder async(boolean async);
    TaskBuilder deferred(long timeAmount, TimeUnit timeValue);
    TaskBuilder timer(long timeAmount, TimeUnit timeValue);
    TaskBuilder executer(Runnable run);
    TaskBuilder reset();
    Task build() throws IllegalArgumentException;

}
