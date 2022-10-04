package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;
public class VoteAction extends RestartAction {

    @Getter
    private HashMap<String, Optional<Boolean>> votes = new HashMap<>();
    private AtomicLong no = new AtomicLong();
    private AtomicLong yes = new AtomicLong();

    public VoteAction() {
        super(RestartType.Vote);
    }

    @Override
    protected void intStart(boolean checkAnnounce){
        timer.set(VoteConfig.getInstance().getVotingTime().getValue());
        timerUnit.set(TimeUnit.SECONDS);
        super.intStart(checkAnnounce);
    }

    public boolean addVote(@NonNull Permission perm, @NonNull String src, @NonNull Optional<Boolean> vote) {
        if (perm.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue())){
            getLogger().info("{} voted {}",src,vote.isPresent()?vote.get():"none");
            votes.put(src, vote);
            return true;
        }
        return false;
    }

    @Override
    protected void doReset() {
        votes = new HashMap<>();
        yes=new AtomicLong(0);
        no=new AtomicLong(0);
        super.doReset();
    }

    @Override
    protected void scoreboard(){
        //this causes too much console spam. Don't do it this way.
//        if (AnnounceConfig.getInstance().getEnableScoreboard().getValue()){
//            updateVotes();
//            API.getBuilder().executer(()->{
//                getLogger().info("Updating Scoreboard");
//                API.getServer().execCommand("say test");
//                API.getServer().execCommand("minecraft:scoreboard objectives remove restart");
//                API.getServer().execCommand("minecraft:scoreboard objectives add restart dummy "+timer.get()+" "+timerUnit.get().toString().toLowerCase());
//                API.getServer().execCommand("minecraft:scoreboard objectives setdisplay sidebar restart");
//                API.getServer().execCommand("minecraft:scoreboard players set yes restart "+yes.get());
//                API.getServer().execCommand("minecraft:scoreboard players set no restart "+no.get());
//            }).name("votereboot-S-updateScoreboard").build();
//        }
    }
    private void updateVotes(){
        yes.set(0);
        no.set(0);
        votes.values().stream().filter(Optional::isPresent).map(Optional::get)
                .forEach(v->{if(v) yes.incrementAndGet();else no.incrementAndGet();});
    }

    @Override
    protected void timerDone() {
        if (restartType==RestartType.Vote){
            API.getBuilder().executer(()->{
                API.getServer().execCommand("minecraft:scoreboard objectives remove restart");
            }).name("votereboot-S-removeScoreboard").build();
            updateVotes();
            double percent = (yes.get() / 1.0 / (yes.get() + no.get())) * 100.0;
            getLogger().info("{} yes, {} no, {}% of the people that voted want the server to be restarted", yes.get(),no.get(),percent);
            getLogger().info("Votes: {}",votes.values().stream().map(e-> e.map(aBoolean -> aBoolean ? "true" : "false").orElse("none")).collect(Collectors.toList()));
            getLogger().info("Voters: {}",votes.keySet());
            if (yes.get() >= VoteConfig.getInstance().getMinAgree().getValue() &&
                    percent >= VoteConfig.getInstance().getPercentToRestart().getValue()) {
                restartType=RestartType.All;
                long votingRestartTime = VoteConfig.getInstance().getVotingRestartTime().getValue();
                timer.set(votingRestartTime);
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartSuccess().getValue().replaceFirst("\\{\\}",Long.toString(votingRestartTime)).replaceFirst("\\{\\}","s"));
            }else {
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartFailed().getValue());
                cancelTimer(true);
                doReset();
            }
        }else{
            //The timer was done once, and the vote was successful
            super.timerDone();
        }
    }

    public boolean isVoteInProgress() {
        return task.isPresent() && timer.get()>=0;
    }


}
