package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.config.TimeEntry;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.pluginapi.convert.Convert;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import com.c0d3m4513r.pluginapi.Task;
import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.ConfigStrings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@AllArgsConstructor
public abstract class RestartAction implements Runnable{
    @Getter
    @NonNull
    protected static Vector<RestartAction> actions = new Vector<>();
    @NonNull
    //Invariant: If this is not-empty, the timer has to be started.
    protected Optional<Task> task = Optional.empty();;
    @NonNull
    protected volatile AtomicLong timer = new AtomicLong();
    @NonNull
    protected volatile AtomicReference<TimeUnit> timerUnit = new AtomicReference<>();
    @NonNull
    @Getter
    protected com.c0d3m4513r.votereboot.reboot.RestartType restartType;
    protected RestartAction(@NonNull com.c0d3m4513r.votereboot.reboot.RestartType restartType) {
        this.restartType = restartType;
        actions.add(this);
        doReset();
    }

    public static Optional<RestartAction> getAction(RestartType type){
        List<RestartAction> actionList = actions.stream().filter(a->a.getRestartType().equals(type))
                .sorted(Comparator.comparing(RestartAction::getTimer))
                .collect(Collectors.toList());
        if(actionList.size()<=0){
            return Optional.empty();
        }else {
            return Optional.of(actionList.get(0));
        }
    }
    TimeUnitValue getTimer(){
        return new TimeUnitValue(timerUnit.get(),timer.get());
    }
    public Optional<TimeUnitValue> getTimer(Permission perm) {
        if (perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Read)))
            return Optional.of(getTimer());
        else return Optional.empty();
    }

    /**
     * Cancels the timer. This needs to be async safe.
     * @return Returns true, if the timer was successfully cancelled.
     */
    private boolean cancelTimer(){
        if (task.isPresent()){
            //This seems to prevent the task from being repeated any longer, no matter the return code.
            task.get().cancel();
            task = Optional.empty();
            return true;
        }else{
            return false;
        }
    }
    protected boolean cancelTimer(boolean del){
        if (del) actions.remove(this);
        return cancelTimer();
    }

    /***
     *
     * @param perm Permission Queryable
     * @return Returns true, if Timer was cancelled
     */
    public boolean cancelTimer(Permission perm){
        if ((perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Cancel))||
                perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Cancel)))
                && task.isPresent()
        ) return cancelTimer(true);
        else return false;
    }

    protected void doReset(){
        cancelTimer(false);
        //we requested a timer cancel. to uphold the invariant, we are going to cancel anyways (even if the cancel was not successful).
        task=Optional.empty();
        timer.set(0);
        timerUnit.set(TimeUnit.SECONDS);
    }
    //todo: does this even make sense?
    public boolean reset(Permission perm){
        if (perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Start))){
            if(task.isPresent()){
                if(!task.get().cancel())
                    return false;
            }
            doReset();
            return true;
        } else return false;
    }

    private void convertTimeLower(){
        if (timer.get()<=2){
            TimeUnit unit = timerUnit.get();
            TimeUnit newUnit = Convert.nextLowerUnitBounded(unit,TimeUnit.SECONDS);
            timer.getAndUpdate(t->newUnit.convert(t,unit));
            timerUnit.set(newUnit);
        }
    }

    protected void intStart(){
        convertTimeLower();
        TimeUnit unit = timerUnit.get();
        if (unit==TimeUnit.MILLISECONDS || unit==TimeUnit.MICROSECONDS || unit==TimeUnit.NANOSECONDS){
            getLogger().warn("[VoteReboot] TimeUnits smaller than Seconds is not supported. Cancelling timer.");
            actions.remove(this);
            cancelTimer();
            return;
        }else{
            task=Optional.of(API.getBuilder()
                    .deferred(1,timerUnit.get())
                    .timer(1,timerUnit.get())
                    .async(true)
                    .name("votereboot-ADR-"+restartType.toString()+actions.indexOf(this))
                    .executer(this)
                    .build()
            );
        }
        getLogger().info("[VoteReboot] Timer(of type {}) started with {} {}", com.c0d3m4513r.votereboot.reboot.RestartType.asString(restartType),timer.get(), timerUnit);
    }
    /**
     * Starts this timer for a reboot
     * @param perm
     * @return Returns true, if a timer was started AND the user has permission. False otherwise.
     */
    public boolean start(Permission perm){
        if (perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Start))
        || perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Start))){
            intStart();
            return true;
        }return false;
    }

    /**
     * This needs to be async safe.
     * This also needs to cancel the timer.
     */
    protected void timerDone(){
        getLogger().trace("[VoteReboot] Timer Done was called. Restarting server now.");
        actions.remove(this);
        API.getServer().sendMessage("The Server is Restarting!");
        API.getBuilder().reset().executer(()->API.getServer().onRestart(Optional.empty())).build();
        cancelTimer();
    }

    /**
     * This needs to be async safe
     * @param timer Time left on the timer
     */
    protected void timerTick(long timer,TimeUnit unit){
        if (Config.getInstance().getActionsEnabled().getValue()){
            String[] atStrings = Config.getInstance().getTimerAnnounceAt().getValue();
            for(val atEntry:atStrings){
                Optional<TimeEntry> teo = TimeEntry.of(atEntry);
                if (teo.isPresent()){
                    TimeEntry te = teo.get();
                    TimeUnitValue tuv = te.getMaxUnit();
                    if (unit.convert(tuv.getValue(),tuv.getUnit())==timer){
                        String announcement = ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(restartType);
                        if (announcement.isEmpty()) announcement=ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.All);
                        API.getServer().sendMessage(announcement.replaceFirst("\\{\\}",Long.toString(timer))
                                .replaceFirst("\\{\\}", String.valueOf(unit)));
                    }
                }else {
                    getLogger().error("[VoteReboot] Cannot parse String as TimeEntry. Please check the config.");
                }
            }
        }
    }

    /**
     * This needs to be async safe
     */
    public void run(){
        long time = timer.getAndDecrement();
        TimeUnit unit = timerUnit.get();
        timerTick(time,unit);
        if (time==2 && unit!=TimeUnit.SECONDS){
            final TimeUnit newUnit;
            switch (unit){
                case DAYS: newUnit =TimeUnit.HOURS; break;
                case HOURS: newUnit =TimeUnit.MINUTES; break;
                case MINUTES:
                case SECONDS:
                    newUnit =TimeUnit.SECONDS; break;
                default:throw new RuntimeException("TimeUnits smaller than Seconds are not supported.");
            }
            timer.getAndUpdate(t->newUnit.convert(t,unit));
            timerUnit.set(newUnit);
        }else if (time<=0){
            getLogger().trace("[VoteReboot] Timer done. Executing timer done function.");
            timerDone();
        }
    }

    public static final Function<Long, LongUnaryOperator> ADD = (add)->(timer)->add+timer;
    public static final Function<Long, LongUnaryOperator> SUB = (sub)->(timer)->timer-sub;

    /***
     *
     * @param perm Permission Queryable
     * @param time Time to be added
     * @param unit The unit of the time parameter
     * @param operation A function to be applied as the operation. First argument is
     * @return Returns true, if the time has been successfully added
     */
    public boolean modifyTimer(Permission perm, long time, TimeUnit unit, Function<Long, LongUnaryOperator> operation){
        if (perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Modify))
            ||perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Modify))) {
            long timerTime = timer.getAndUpdate(operation.apply(timerUnit.get().convert(time,unit)));
            return true;
        }else return false;
    }

}
