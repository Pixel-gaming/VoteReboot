package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.VoteConfig;
import com.c0d3m4513r.votereboot.config.ConfigStrings;
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
    protected void intStart(){
        timer.set(VoteConfig.getInstance().getVotingTime().getValue());
        timerUnit.set(TimeUnit.SECONDS);
        super.intStart();
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
    protected void timerDone() {
        if (restartType==RestartType.Vote){
            votes.values().stream().filter(Optional::isPresent).map(Optional::get)
                    .forEach(v->{if(v) yes.incrementAndGet();else no.incrementAndGet();});
            double percent = (yes.get() / 1.0 / (yes.get() + no.get())) * 100.0;
            getLogger().info("{} yes, {} no, {}% of the people that voted want the server to be restarted", yes.get(),no.get(),percent);
            getLogger().info("Votes: {}",votes.values().stream().map(e-> e.map(aBoolean -> aBoolean ? "true" : "false").orElse("none")).collect(Collectors.toList()));
            getLogger().info("Voters: {}",votes.keySet());
            if (yes.get() >= ((Config) API.getConfig()).getVoteConfig().getMinAgree().getValue() &&
                    percent >= ((Config) API.getConfig()).getVoteConfig().getPercentToRestart().getValue()) {
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
