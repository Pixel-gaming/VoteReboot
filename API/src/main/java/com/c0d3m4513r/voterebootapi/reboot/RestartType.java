package com.c0d3m4513r.voterebootapi.reboot;

import com.c0d3m4513r.voterebootapi.RSMCPerm;
import com.c0d3m4513r.voterebootapi.config.VoteConfigPermission;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RestartType {
    Vote(VoteConfigPermission.getVoteAction()),
    ManualRestart(VoteConfigPermission.getManualAction()),
    Scheduled(VoteConfigPermission.getScheduledAction());
    public final RSMCPerm perm;
}
