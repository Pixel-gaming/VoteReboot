package com.c0d3m4513r.pluginapi;

import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import com.c0d3m4513r.pluginapi.messages.MessageReceiver;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Server implements MessageReceiver {

    public void onRestart(Optional<String> reason){
        EventRegistrar.submitEvent(EventType.onReboot);
        restart(reason);
    }

    public abstract void execCommand(@NonNull String cmd);

    protected abstract boolean restart(Optional<String> reason);

    public abstract Set<Task> getTasks();

}
