package com.c0d3m4513r.voterebootapi;

import com.c0d3m4513r.voterebootapi.config.VoteConfig;
import lombok.NonNull;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public interface Server {
    boolean sendMessage(@NonNull String string);

    default void onRestart(){
        onBeforeRestart();
        restart();
    }

    default void onBeforeRestart(){
        if(VoteConfig.getActionsEnabled().getValue())
            Arrays.stream(VoteConfig.getRebootCommands().getValue()).forEachOrdered(this::execCommand);
    }

    void execCommand(@NonNull String cmd);

    boolean restart();
    default void voteRestart(){
        sendMessage("A Vote has determined, that the server should be restarted");
        API.builder.deferred(1, TimeUnit.SECONDS).executer(this::onRestart).build();
    }
    default void voteFailed(){
        sendMessage("A Vote has determined, that the server should not be restarted");
    }

}
