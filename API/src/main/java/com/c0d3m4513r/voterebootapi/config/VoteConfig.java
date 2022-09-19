package com.c0d3m4513r.voterebootapi.config;

import lombok.Getter;

import java.util.*;

public class VoteConfig {
    @Getter
    public static VoteConfigPermission voteConfigPermission;
    @Getter
    public static VoteConfigStrings voteConfigStrings;
    @Getter
    private static ConfigEntry<Double> percentToRestart = new ConfigEntry<>(100.0,"votereboot.reboot.percentToRestart");
    @Getter
    private static ConfigEntry<Integer> minAgree = new ConfigEntry<>(Integer.MAX_VALUE,"votereboot.reboot.minAgree");
    @Getter
    private static ConfigEntry<Integer> votingTime = new ConfigEntry<>(100,"votereboot.reboot.votingTime");

    @Getter
    private static ConfigEntry<List<String>> yesList = new ConfigEntry<>(Arrays.asList("yes", "ye", "y","t","true"),
            "votereboot.reboot.yesList");

    @Getter
    private static ConfigEntry<List<String>> noList = new ConfigEntry<>(Arrays.asList("no", "n","f","false"),
            "votereboot.reboot.noList");
    @Getter
    private static ConfigEntry<List<String>> noneList = new ConfigEntry<>(Arrays.asList("null", "none"),
            "votereboot.reboot.noneList");
    @Getter
    private static ConfigEntry<Boolean> kickEnabled = new ConfigEntry<>(true, "votereboot.reboot.kick.enabled");
    @Getter
    private static ConfigEntry<Boolean> useCustomMessage = new ConfigEntry<>(false, "votereboot.reboot.kick.useCustomMessage");

    @Getter
    private static ConfigEntry<String> customMessage = new ConfigEntry<>(
            "The Server is Restarting!",
            "votereboot.reboot.kick.useCustomMessage");
    @Getter
    private static ConfigEntry<Boolean> actionsEnabled = new ConfigEntry<>(true,"votereboot.reboot.actions.enabled");
    @Getter
    private static ConfigEntry<String[]> rebootCommands = new ConfigEntry<>(new String[]{"save-all"},"votereboot.reboot.actions.commands");

    public VoteConfigPermission getVoteConfigPermission() {
        return VoteConfig.voteConfigPermission;
    }
    public VoteConfigStrings getVoteConfigStrings() {
        return VoteConfig.voteConfigStrings;
    }
}
