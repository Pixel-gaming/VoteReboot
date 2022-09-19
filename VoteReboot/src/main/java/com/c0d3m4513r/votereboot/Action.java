package com.c0d3m4513r.votereboot;

public enum Action {
    Read(0),
    Start(1),
    Modify(2),
    Cancel(3);
    public final long id;
    /***
     * Holds the Maximum id +1 in this enum
     */
    public static final long MAX_ID=4;
    Action(long id){
        this.id=id;
    }

    public String toString(Action action){
        switch (action){
            case Read:return "read";
            case Start:return "start";
            case Modify:return "modify";
            case Cancel:return "cancel";
            default: throw new Error("Enum has more variants than expected");
        }
    }
}
