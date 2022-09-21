package com.c0d3m4513r.pluginapi.events;

import com.c0d3m4513r.pluginapi.API;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public EventRegistrar(Runnable runnable, EventType eventType,int ttl){
        this(runnable,eventType);
        TTL=ttl;
    }

    public void unregister(){
        EventRegistrar.events.remove(this);
    }

    public static void submitEvent(EventType type){
        Stream<EventRegistrar> stream = events.stream().filter(e -> e.event==type);
        API.getLogger().info("[API] Running event '"+type.toString()+"'.");
        stream.forEach(e->{e.TTL-=1;e.runnable.run();});
        List<EventRegistrar> list = events.stream().filter(e -> e.TTL<0).collect(Collectors.toList());
        API.getLogger().info("[API] Removing "+list.size()+" events from the event listener, since the TTL ran out.");
        events.removeAll(list);
    }
}
