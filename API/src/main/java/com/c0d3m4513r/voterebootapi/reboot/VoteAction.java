package com.c0d3m4513r.voterebootapi.reboot;

import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.Nullable;
import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.config.VoteConfig;
import com.c0d3m4513r.voterebootapi.config.VoteConfigPermission;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class VoteAction extends Action {

    @Getter
    private HashMap<String, Optional<Boolean>> votes = new HashMap<>();
    private final AtomicLong no = new AtomicLong();
    private final AtomicLong yes = new AtomicLong();

    public VoteAction() {
        super(RestartType.Vote);
        timer.set(VoteConfig.getVotingTime().getValue());
        timerUnit=TimeUnit.SECONDS;
    }

    public boolean addVote(@NonNull Permission perm, @NonNull String src, @NonNull Optional<Boolean> vote) {
        if (perm.hasPerm(VoteConfigPermission.getVoteRegister().getValue())){
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
        if (yes.get() >= VoteConfig.getMinAgree().getValue() &&
                yes.get() / 1.0 / (yes.get() + no.get()) * 100.0 < VoteConfig.getPercentToRestart().getValue()) {
            API.getServer().voteRestart();
        }else {
            API.getServer().voteFailed();
        }
    }

    public boolean isVoteInProgress() {
        return timer.get()>=0;
    }


}
