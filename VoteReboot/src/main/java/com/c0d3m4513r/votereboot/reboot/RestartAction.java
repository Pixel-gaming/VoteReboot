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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
    protected Optional<Task> task = Optional.empty();
    @NonNull
    //It is okay for the timer to lag behind up to (exclusive) 1 timerUnit.
    //That is because the time in between is tracked by the server because of Task repeat intervals.
    protected volatile AtomicLong timer = new AtomicLong();
    @NonNull
    protected volatile AtomicReference<TimeUnit> timerUnit = new AtomicReference<>();
    @NonNull
    @Getter
    protected com.c0d3m4513r.votereboot.reboot.RestartType restartType;
    @Getter
    private final int id;
    private final static AtomicInteger CurrentMaxTaskId = new AtomicInteger(0);
    protected RestartAction(@NonNull com.c0d3m4513r.votereboot.reboot.RestartType restartType) {
        this.restartType = restartType;
        actions.add(this);
        id=CurrentMaxTaskId.incrementAndGet();
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
    protected final boolean cancelTimer(boolean del){
        if (del) actions.remove(this);
        return cancelTimer();
    }

    /***
     *
     * @param perm Permission Queryable
     * @return Returns true, if Timer was cancelled
     */
    protected final boolean cancelTimer(Permission perm,boolean del){
        if ((perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Cancel))||
                perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Cancel)))
                && task.isPresent()
        ) return cancelTimer(del);
        else return false;
    }

    /***
     *
     * @param perm Permission Queryable
     * @return Returns true, if Timer was cancelled
     */
    public final boolean cancelTimer(Permission perm){
        return cancelTimer(perm,true);
    }

    /***
     *
     * @param perm Permission Queryable
     * @param ids id(s) of the timer to be cancelled
     * @return Returns number of cancelled tasks
     */
    public static int cancel(Permission perm, HashSet<Integer> ids){
        AtomicInteger integer = new AtomicInteger(0);

        actions.removeAll(
                actions
                .stream().parallel()
                //is that the correct id?
                .filter(a->ids.contains(a.getId()))
                //cancel the id, if permissible. If not don't ignore.
                //We need the RebootAction objects for the removeAll.
                //Therefore, we cannot just map and filter or something.
                .filter(a->
                    {
                        if (a.cancelTimer(perm,false)){
                            integer.incrementAndGet();
                            return true;
                        }else return false;
                    })
                .collect(Collectors.toList())
        );
        return integer.get();
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

    protected void intStart(boolean checkAnnounce){
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
                    .name("votereboot-ADR-"+ restartType +id)
                    .executer(this)
                    .build()
            );
        }
        getLogger().info("[VoteReboot] Timer(of type {}) started with {} {}", com.c0d3m4513r.votereboot.reboot.RestartType.asString(restartType),timer.get(), timerUnit);
        if(checkAnnounce){
            long time = timer.get();
            Optional<TimeUnitValue> omax = Config.getInstance()
                    .getTimerAnnounceAt().getValue()
                    .stream()
                    .map(TimeEntry::of)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(TimeEntry::getMaxUnit)
                    .max(TimeUnitValue::compareTo);
            for (val val:Config.getInstance().getTimerAnnounceAt().getValue()){
                Optional<TimeEntry> tuv = TimeEntry.of(val);
                if (tuv.isPresent() && (new TimeUnitValue(unit,time)).compareTo(tuv.get().getMaxUnit())>=0){
                    timerAnnounce(time,unit);
                    return;
                }

            }
        }
    }
    /**
     * Starts this timer for a reboot
     * @param perm Permission Queryable object of command executer to check for permission
     * @return Returns true, if a timer was started AND the user has permission. False otherwise.
     */
    public boolean start(Permission perm){
        if (perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(Action.Start))
        || perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Start))){
            intStart(true);
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

    private void timerAnnounce(long timer,TimeUnit unit){
        getLogger().info("Announcing {} timer = {} {}",restartType,timer,unit);
        String announcement = ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(restartType);
        if (announcement.isEmpty()) announcement=ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.All);
        API.getServer().sendMessage(announcement.replaceFirst("\\{\\}",Long.toString(timer))
                .replaceFirst("\\{\\}", String.valueOf(unit)));
    }
    /**
     * This needs to be async safe
     * @param timer Time left on the timer
     */
    private void timerTick(long timer,TimeUnit unit){
        if (Config.getInstance().getEnableTimerAnnounce().getValue()){
            List<String> atStrings = Config.getInstance().getTimerAnnounceAt().getValue();
            for(val atEntry:atStrings){
                Optional<TimeEntry> teo = TimeEntry.of(atEntry);
                if (teo.isPresent()){
                    TimeEntry te = teo.get();
                    TimeUnitValue tuv = te.getMaxUnit();
                    if (unit.convert(tuv.getValue(),tuv.getUnit())==timer){
                        timerAnnounce(tuv.getValue(),tuv.getUnit());
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
        long time = timer.decrementAndGet();
        TimeUnit unit = timerUnit.get();
        timerTick(time,unit);
        if (time==1 && unit!=TimeUnit.SECONDS){
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
            cancelTimer(false);
            intStart(false);
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
