package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.Action;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RestartType {
    Vote(0),
    Manual(1),
    Scheduled(2),
    All(3);

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
            case Manual: return "manual";
            case Vote: return "vote";
            case All: return "all";
            default: throw new Error("Enum has more variants than expected");
        }
    }
}
