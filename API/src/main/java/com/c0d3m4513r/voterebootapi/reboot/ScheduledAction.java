package com.c0d3m4513r.voterebootapi.reboot;

public class ScheduledAction extends Action implements Runnable{
    ScheduledAction(){
        super(RestartType.Scheduled);
    }
}
