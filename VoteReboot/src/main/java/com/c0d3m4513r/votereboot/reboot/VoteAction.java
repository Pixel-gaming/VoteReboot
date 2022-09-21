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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class VoteAction extends RestartAction {

    @Getter
    private HashMap<String, Optional<Boolean>> votes = new HashMap<>();
    private final AtomicLong no = new AtomicLong();
    private final AtomicLong yes = new AtomicLong();

    public VoteAction() {
        super(RestartType.Vote);
        timer.set(((Config)API.getConfig()).getVoteConfig().getVotingTime().getValue());
        timerUnit.set(TimeUnit.SECONDS);
    }

    public boolean addVote(@NonNull Permission perm, @NonNull String src, @NonNull Optional<Boolean> vote) {
        if (perm.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue())){
            votes.put(src, vote);
            return true;
        }
        return false;
    }

    @Override
    protected void doReset() {
        votes = new HashMap<>();
        yes.set(0);
        no.set(0);
        super.doReset();
    }

    @Override
    protected void timerDone() {
        if (restartType==RestartType.Vote){
            if (yes.get() >= ((Config) API.getConfig()).getVoteConfig().getMinAgree().getValue() &&
                    yes.get() / 1.0 / (yes.get() + no.get()) * 100.0 < ((Config) API.getConfig()).getVoteConfig().getPercentToRestart().getValue()) {
                restartType=RestartType.All;
                long votingRestartTime = VoteConfig.getInstance().getVotingRestartTime().getValue();
                timer.getAndUpdate(ADD.apply(votingRestartTime));
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartSuccess().getValue().replaceFirst("\\{\\}",Long.toString(votingRestartTime)).replaceFirst("\\{\\}","s"));
            }else {
                API.getServer().sendMessage(ConfigStrings.getInstance().getVoteRestartFailed().getValue());
                cancelTimer();
            }
        }else{
            //The timer was done once, and the vote was successful
            super.timerDone();
        }
    }

    public boolean isVoteInProgress() {
        return timer.get()>=0;
    }


}
