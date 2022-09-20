package com.c0d3m4513r.votereboot.reboot;

public class ScheduledAction extends RestartAction implements Runnable{
    ScheduledAction(){
        super(RestartType.Scheduled);
    }
}
