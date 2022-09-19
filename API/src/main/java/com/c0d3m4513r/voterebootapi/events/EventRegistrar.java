package com.c0d3m4513r.voterebootapi.events;

import lombok.AllArgsConstructor;

import java.util.Vector;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EventRegistrar {
    private static Vector<EventRegistrar> events = new Vector<>();

    Runnable runnable;
    EventType event;
    int TTL = 0;

    private EventRegistrar(Runnable runnable, EventType eventType){
        this.runnable=runnable;
        this.event=eventType;
        events.add(this);
    }

    public void unregister(){
        EventRegistrar.events.remove(this);
    }

    public static void submitEvent(EventType type){
        events.removeAll(
                events.stream().filter(e -> e.event==type).filter(e -> {
                    e.runnable.run();
                    e.TTL-=1;
                    return e.TTL<0;
                }).collect(Collectors.toList()));
    }
}
