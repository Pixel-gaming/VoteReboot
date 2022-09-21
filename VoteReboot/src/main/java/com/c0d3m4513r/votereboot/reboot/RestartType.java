package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.ActionPerm;
import com.c0d3m4513r.votereboot.commands.RebootSubcommands;
import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.pluginapi.API;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RestartType {
    Vote(0),
    ManualRestart(1),
    Scheduled(2),
    All(3);

    @NonNull
    public final long id;

    /***
     * @return Gets the Maximum id +1 in this enum.
     * This function should not return different values, when this class has not been changed.
     */
    public static long getMaxId(){
        Optional<Long> ol = Arrays.stream(Action.values()).map(e->e.id).max(Long::compareTo);
        if (ol.isPresent()){
            return ol.get()+1;
        }else{
            return Action.values().length;
        }
    }
    public static String asString(RestartType type){
        switch (type){
            case Scheduled: return "scheduled";
            case ManualRestart: return "manual";
            case Vote: return "vote";
            case All: return "all";
            default: throw new Error("Enum has more variants than expected");
        }
    }
}
