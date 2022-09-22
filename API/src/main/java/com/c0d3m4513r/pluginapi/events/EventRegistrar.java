package com.c0d3m4513r.pluginapi.events;

import com.c0d3m4513r.pluginapi.API;
import lombok.val;

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

    /**
     * Execution of this function assumes, that we are on the main server thread.
     */
    public static void submitEvent(EventType type){
        List<EventRegistrar> stream = events.stream().filter(e -> e.event==type).collect(Collectors.toList());
        API.getLogger().info("[API] Running event '"+type.toString()+"' for "+stream.size()+" Events.");
        //Note: Do not work with streams, because of funny concurrent modification exceptions.
        int removed=0;
        for (int i = 0; i<stream.size();i++){
            val e = stream.get(i-removed);
            e.TTL-=1;
            e.runnable.run();
            if (e.TTL>=0){
                events.remove(e);
                removed+=1;
            }
        }
        API.getLogger().info("[API] Removing "+removed+" events from the event listener, since the TTL ran out.");
    }
}
