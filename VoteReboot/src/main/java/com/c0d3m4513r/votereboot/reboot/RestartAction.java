package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.Task;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

@AllArgsConstructor
public abstract class RestartAction implements Runnable{
    @NonNull
    public static Vector<RestartAction> actions = new Vector<>();
    @NonNull
    protected Optional<Task> task;
    @NonNull
    protected volatile AtomicLong timer;
    @NonNull
    protected TimeUnit timerUnit;
    @NonNull
    public final RestartType restartType;
    protected RestartAction(@NonNull RestartType restartType) {
        this.restartType = restartType;
        actions.add(this);
        doReset();
    }

    public long getTimerPermless(){
        return timer.get();
    }
    public Optional<Long> getTimer(Permission perm) {
        if (perm.hasPerm(restartType.perm.getPermission(Action.Read)))
            return Optional.of(timer.get());
        else return Optional.empty();
    }

    public Optional<TimeUnit> getTimerUnit(Permission perm) {
        if (perm.hasPerm(restartType.perm.getPermission(Action.Read)))
            return Optional.of(timerUnit);
        else return Optional.empty();
    }

    /***
     *
     * @param perm Permission Queryable
     * @return Returns true, if Timer was cancelled
     */
    public boolean cancelTimer(Permission perm){
        if (perm.hasPerm(restartType.perm.getPermission(Action.Cancel)) && task.isPresent()) return task.get().cancel();
        else return false;
    }

    protected void doReset(){
        task=Optional.empty();
        timer=new AtomicLong();
        timerUnit=TimeUnit.SECONDS;
    }
    public boolean reset(Permission perm){
        if (perm.hasPerm(restartType.perm.getPermission(Action.Start))){
            if(task.isPresent()){
                if(!task.get().cancel())
                    return false;
            }
            doReset();
            return true;
        } else return false;
    }
    public boolean start(Permission perm){
        if (perm.hasPerm(restartType.perm.getPermission(Action.Start))){
            task=Optional.of(API.getBuilder()
                    .deferred(1,timerUnit)
                    .timer(1,timerUnit)
                    .async(true)
                    .executer(this)
                    .build()
            );
            return true;
        }return false;
    }

    protected void timerDone(){
        API.getServer().sendMessage("The Server is Restarting!");
        API.getServer().onRestart();
    }
    protected void timerTick(long timer){

    }
    public void run(){
        long time = timer.getAndDecrement();
        if (time<=0){
            API.getBuilder().executer(this::timerDone);
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
        if (perm.hasPerm(restartType.perm.getPermission(Action.Modify))) {
            long timerTime = timer.getAndUpdate(operation.apply(timerUnit.convert(time,unit)));
            return true;
        }else return false;
    }

}
