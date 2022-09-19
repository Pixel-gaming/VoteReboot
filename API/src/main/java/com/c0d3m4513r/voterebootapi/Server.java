package com.c0d3m4513r.voterebootapi;

import com.c0d3m4513r.voterebootapi.events.EventRegistrar;
import com.c0d3m4513r.voterebootapi.events.EventType;
import com.c0d3m4513r.voterebootapi.messages.MessageReceiver;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class Server implements MessageReceiver {

    public void onRestart(){
        EventRegistrar.submitEvent(EventType.onReboot);
        restart(Optional.empty());
    }

    public abstract void execCommand(@NonNull String cmd);

    public abstract boolean restart(Optional<String> reason);
    void voteRestart(){
        sendMessage("A Vote has determined, that the server should be restarted");
        API.builder.deferred(1, TimeUnit.SECONDS).executer(this::onRestart).build();
    }
    void voteFailed(){
        sendMessage("A Vote has determined, that the server should not be restarted");
    }

}
