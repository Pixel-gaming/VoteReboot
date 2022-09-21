package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Permission;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class VoteAction extends RestartAction {

    @Getter
    private HashMap<String, Optional<Boolean>> votes = new HashMap<>();
    private final AtomicLong no = new AtomicLong();
    private final AtomicLong yes = new AtomicLong();

    public VoteAction() {
        super(RestartType.Vote);
        timer.set(((Config)API.getConfig()).getVoteConfig().getVotingTime().getValue());
        timerUnit=TimeUnit.SECONDS;
    }

    public boolean addVote(@NonNull Permission perm, @NonNull String src, @NonNull Optional<Boolean> vote) {
        if (perm.hasPerm(((Config) API.getConfig()).getVoteConfig().getVoteConfigPermission().getVoteRegister().getValue())){
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
        if (yes.get() >= ((Config) API.getConfig()).getVoteConfig().getMinAgree().getValue() &&
                yes.get() / 1.0 / (yes.get() + no.get()) * 100.0 < ((Config) API.getConfig()).getVoteConfig().getPercentToRestart().getValue()) {
            //todo: vote Success impl
        }else {
            //todo: vote Failed impl
        }
    }

    public boolean isVoteInProgress() {
        return timer.get()>=0;
    }


}
