package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.Data.Point3D;
import com.c0d3m4513r.pluginapi.TaskBuilder;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.pluginapi.config.TimeEntry;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.pluginapi.convert.Convert;
import com.c0d3m4513r.pluginapi.messages.Title;
import com.c0d3m4513r.pluginapi.registry.Sound;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import com.c0d3m4513r.pluginapi.Task;
import com.c0d3m4513r.votereboot.config.AnnounceConfig;
import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.ConfigStrings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.*;
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
    protected AtomicLong timer = new AtomicLong();
    @NonNull
    protected AtomicReference<TimeUnit> timerUnit = new AtomicReference<>();
    @NonNull
    @Getter
    protected com.c0d3m4513r.votereboot.reboot.RestartType restartType;
    @Getter
    private final int id;
    @Getter
    protected String reason = null;

    //if set, this restart action will override everything. (no other reboots will trigger)
    //just setting this is not enough btw. The timer has to also be started. (task is non-null)
    //only used for manual reboots so far
    @Getter
    protected static RestartAction override = null;
    private final static AtomicInteger CurrentMaxTaskId = new AtomicInteger(0);
    protected RestartAction(@NonNull com.c0d3m4513r.votereboot.reboot.RestartType restartType, @NonNull TimeUnitValue tuv) {
        doReset();
        timer.set(tuv.getValue());
        timerUnit.set(tuv.getUnit());
        this.restartType = restartType;
        actions.add(this);
        id=CurrentMaxTaskId.incrementAndGet();
    }

    public static Optional<RestartAction> getAction(RestartType type){
        List<RestartAction> actionList = actions.stream().filter(a->a.getRestartType().equals(type))
                .sorted(Comparator.comparing(RestartAction::getTimer))
                .collect(Collectors.toList());
        if(actionList.size() == 0){
            return Optional.empty();
        }else {
            return Optional.of(actionList.get(0));
        }
    }
    TimeUnitValue getTimer(){
        return new TimeUnitValue(timerUnit.get(),timer.get());
    }

    protected static boolean hasPerm(Permission perm, RestartType restartType, Action action){
        return perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(restartType).getPermission(action))
                || perm.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(RestartType.All).getPermission(action));
    }
    public Optional<TimeUnitValue> getTimer(Permission perm) {
        if (hasPerm(perm, restartType, Action.Read))
            return Optional.of(getTimer());
        else return Optional.empty();
    }
    public static boolean isOverridden(){
        return override != null && override.task.isPresent();
    }

    /**
     * Cancels the timer. This needs to be async safe.
     * @return Returns true, if the timer was successfully cancelled.
     */
    private boolean cancelTimer(){
        if(override == this) override = null;
        if (task.isPresent()){
            if(shouldAnnounce(true)){
                getServer().sendMessage("The lowest timer has been cancelled. The next announce will be for a different timer, and have a larger reboot time.");
            }
            //This seems to prevent the task from being repeated any longer, no matter the return code.
            task.get().cancel();
            task = Optional.empty();
            return true;
        }else{
            return false;
        }
    }
    //async
    protected boolean cancelTimer(boolean del){
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
    //async
    private void convertTimeLower(){
        if (timer.get()<=2){
            TimeUnit unit = timerUnit.get();
            TimeUnit newUnit = Convert.nextLowerUnitBounded(unit,TimeUnit.SECONDS);
            timer.getAndUpdate(t->newUnit.convert(t,unit));
            timerUnit.set(newUnit);
        }
    }
    //async
    protected void intStart(boolean checkAnnounce){
        convertTimeLower();
        TimeUnit unit = timerUnit.get();
        if (unit==TimeUnit.MILLISECONDS || unit==TimeUnit.MICROSECONDS || unit==TimeUnit.NANOSECONDS){
            getLogger().warn("[VoteReboot] TimeUnits smaller than Seconds is not supported. Cancelling timer.");
            cancelTimer(true);
            return;
        }
        task=Optional.of(
                TaskBuilder.builder()
                .deferred(1,timerUnit.get())
                .timer(1,timerUnit.get())
                .async(true)
                .name("votereboot-ADR-"+ restartType +id)
                .executer(this)
                .build()
        );
        getLogger().info("[VoteReboot] Timer(of type {}) started with {} {}", com.c0d3m4513r.votereboot.reboot.RestartType.asString(restartType),timer.get(), timerUnit);
        if(requestTimerAnnounce(true)){
            getServer().sendMessage("A new timer with a smaller Reboot time has been started.");
        }
    }
    /**
     * Starts this timer for a reboot
     * @param perm Permission Queryable object of command executer to check for permission
     * @return Returns true, if a timer was started AND the user has permission. False otherwise.
     */
    public boolean start(CommandSource perm){
        if (isOverridden() && this != override && this.restartType != RestartType.Manual){
            perm.sendMessage("Starting timers is currently disabled, because there is a timer overriding them.");
            return false;
        }
        if (hasPerm(perm, restartType, Action.Start) ){
            intStart(true);
            return true;
        }return false;
    }

    /**
     * This needs to be async safe.
     * This also needs to cancel the timer.
     */
    //async
    protected void timerDone(){
        //always cancel timer
        cancelTimer(true);
        if (isOverridden()) return;
        //but only reboot if we are not currently overridden
        getLogger().trace("[VoteReboot] Timer Done was called. Restarting server now.");
        getServer().sendMessage("The Server is Restarting!");
        String reason = null;
        if (Config.getInstance().getUseCustomMessage().getValue()){
            reason=Config.getInstance().getCustomMessage().getValue();
        }
        String finalReason = reason;
        runOnMain(()-> getServer().onRestart(Optional.ofNullable(finalReason)));
    }
    private static Optional<RestartAction> getLowestTimer(){
        if (actions.size() <= 0) return Optional.empty();

        RestartAction lowest = actions.get(0);
        for (val action : actions) {
            if (action.getTimer().compareTo(lowest.getTimer()) < 0) lowest = action;
        }
        return Optional.of(lowest);
    }
    private boolean shouldAnnounce(boolean justStarted){
        //I only ever want to announce the override timer, if it exists
        if(isOverridden() && this != override) return false;

        RestartAction ra = isOverridden()?override:getLowestTimer().orElse(this);
        //only bother checking the announcement times, if this is actually the timer what should be announced
        //or if we want to check if other timers are below the announcement threshold.
        if (!justStarted && this != ra) return false;

        BiFunction<TimeUnitValue,TimeUnitValue,Boolean> comparator;
        //if any announcement timer is below the starting timer, we want to announce the newer timer
        if (justStarted) comparator = (announceTimer, timerValue) -> announceTimer.compareTo(timerValue) > 0;
        //else only announce if we found an announcement with that particular timer
        else comparator = (announceTimer, timerValue) -> announceTimer.compareTo(timerValue) == 0;

        getLogger().info("Checking timers");
        for (val val:AnnounceConfig.getInstance().getTimerAnnounceAt().getValue()){
            Optional<TimeEntry> te = TimeEntry.of(val);
            if (!te.isPresent()) continue;
            TimeUnitValue tuv = te.get().getMaxUnit();
            if (comparator.apply(tuv, ra.getTimer())) {
                return true;
            }
        }
        return false;
    }
    private boolean requestTimerAnnounce() {
        return requestTimerAnnounce(false);
    }
    private boolean requestTimerAnnounce(boolean justStarted){
        if ((justStarted && shouldAnnounce(true))
                || shouldAnnounce(false)) {
            timerAnnounce();
            return true;
        }
        return false;
    }
    //async
    private void timerAnnounce(){
        long timer = this.timer.get();
        TimeUnit unit = timerUnit.get();
        getLogger().info("Announcing {} timer = {} {}",restartType,timer,unit);
        String announcement = ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(restartType);
        if (announcement.isEmpty()) announcement=ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.All);
        //Only do chat announcements, if really enabled.
        if(AnnounceConfig.getInstance().getEnableTimerChatAnnounce().getValue()){
            getServer().sendMessage(announcement.replaceFirst("\\{\\}",Long.toString(timer))
                .replaceFirst("\\{\\}", String.valueOf(unit)));
            if (reason != null)
                getServer().sendMessage("Reason: "+reason);
        }
        soundTimerAnnounce();
        //Only do title announcements, if titles are enabled
        if (AnnounceConfig.getInstance().getEnableTitle().getValue()){
            runOnMain(()->{
                for(val world: getServer().getWorlds()){
                    val title = new Title(
                            Optional.ofNullable(reason),
                            Optional.of(ConfigStrings.getInstance()
                            .getServerRestartAnnouncement()
                            .getPermission(restartType)
                            .replaceFirst("\\{\\}",Long.toString(timer))
                            .replaceFirst("\\{\\}",unit.toString().toLowerCase()))
                    );
                    world.sendTitle(title);
                }
            });
        }
    }
    //async
    private void soundTimerAnnounce(){
        if(AnnounceConfig.getInstance().getEnableTimerSoundAnnounce().getValue()){
            Sound sound = Sound.getType(AnnounceConfig.getInstance().getSoundId().getValue());
            int volume = AnnounceConfig.getInstance().getSoundAnnounceVolume().getValue();
            for (val world: getServer().getWorlds()){
                if(AnnounceConfig.getInstance().getSoundAnnouncePlayGlobal().getValue())
                    world.playSound(sound, world.getSpawnLocation(), volume);
                else
                    for (val player:world.getPlayers()){
                        player.playSound(sound,player.getLocation(),volume);
                    }
            }
        }
    }
    //async
    protected void scoreboard(){}
    /**
     * This needs to be async safe
     * @param timer Time left on the timer
     */
    //async
    private void timerTick(long timer,TimeUnit unit){
        requestTimerAnnounce();
    }

    /**
     * This needs to be async safe
     */
    //async
    public void run(){
        long time = timer.decrementAndGet();
        TimeUnit unit = timerUnit.get();
        scoreboard();
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
