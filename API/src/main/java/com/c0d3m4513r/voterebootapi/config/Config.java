package com.c0d3m4513r.voterebootapi.config;


import lombok.Getter;

public class Config {
    @Getter
    public static VoteConfig voteConfig = null;

    public VoteConfig getVoteConfig() {
        return Config.voteConfig;
    }
}
