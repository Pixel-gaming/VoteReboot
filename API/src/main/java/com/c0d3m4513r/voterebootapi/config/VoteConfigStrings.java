package com.c0d3m4513r.voterebootapi.config;

import lombok.Getter;

public class VoteConfigStrings {
    @Getter
    private static ConfigEntry<String> noVoteActive = new ConfigEntry<>(
            "No vote was running. To start a vote just type /reboot vote",
            "votereboot.vote.translate.noVoteActive"
    );
    @Getter
    private static ConfigEntry<String> voteYesNoError = new ConfigEntry<>(
            "The vote answer was not recognised as either yes or no. Please try again.",
            "votereboot.vote.translate.voteYesNoError");

    @Getter
    private static ConfigEntry<String> noPermission = new ConfigEntry<>(
            "The Action failed, or you did not have enough permissions.",
            "votereboot.vote.translate.noPermission");

    @Getter
    private static ConfigEntry<String> requiredArgs = new ConfigEntry<>(
            "Some required Arguments were missing",
            "votereboot.vote.translate.requiredArgs");

    //todo: complete this
}
