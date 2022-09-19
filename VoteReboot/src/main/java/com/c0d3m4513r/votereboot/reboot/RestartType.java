package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.votereboot.ActionPerm;
import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.votereboot.config.VoteConfigPermission;
import com.c0d3m4513r.voterebootapi.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RestartType {
    Vote(((Config)API.getConfig()).getVoteConfig().getVoteConfigPermission().getVoteAction()),
    ManualRestart(((Config)API.getConfig()).getVoteConfig().getVoteConfigPermission().getManualAction()),
    Scheduled(((Config)API.getConfig()).getVoteConfig().getVoteConfigPermission().getScheduledAction());
    public final ActionPerm perm;
}
