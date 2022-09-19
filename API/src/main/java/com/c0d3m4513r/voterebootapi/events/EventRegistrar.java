package com.c0d3m4513r.voterebootapi.events;

import java.util.Vector;

public class EventRegistrar {
    private static Vector<EventRegistrar> events = new Vector<>();

    Runnable runnable;
    EventType event;

    public EventRegistrar(Runnable runnable, EventType eventType){
        this.runnable=runnable;
        this.event=eventType;
        events.add(this);
    }

    public void unregister(){
        EventRegistrar.events.remove(this);
    }

    public static void submitEvent(EventType type){
        events.stream().filter(e -> e.event==type).forEach(e -> e.runnable.run());
    }
}
