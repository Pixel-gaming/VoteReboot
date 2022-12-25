package com.c0d3m4513r.votereboot;

import java.util.Arrays;
import java.util.Optional;

public enum Action {
    Read(0),
    Start(1),
    Modify(2),
    Cancel(3);
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
    Action(long id){
        this.id=id;
    }
}
