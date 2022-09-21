package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfigStrings implements IConfigLoadableSaveable {
    public static VoteConfigStrings getInstance(){
        return VoteConfig.getInstance().getVoteConfigStrings();
    }
    @NonNull
    private ConfigEntry<String> noVoteActive = new ConfigEntry<>(
            new ClassValue<>("No vote was running. To start a vote just type /reboot vote",String.class),
            "votereboot.vote.translate.noVoteActive"
    );
    @NonNull
    private ConfigEntry<String> voteYesNoError = new ConfigEntry<>(
            new ClassValue<>("The vote answer was not recognised as either yes or no. Please try again.",String.class),
            "votereboot.vote.translate.voteYesNoError");

    @NonNull
    private ConfigEntry<String> noPermission = new ConfigEntry<>(
            new ClassValue<>("The Action failed, or you did not have enough permissions.",String.class),
            "votereboot.vote.translate.noPermission");

    @NonNull
    private ConfigEntry<String> requiredArgs = new ConfigEntry<>(
            new ClassValue<>("Some required Arguments were missing",String.class),
            "votereboot.vote.translate.requiredArgs");

    @Override
    public void loadValue() {
        noVoteActive.loadValue();
        voteYesNoError.loadValue();
        noPermission.loadValue();
        requiredArgs.loadValue();
    }

    @Override
    public void saveValue() {
        noVoteActive.saveValue();
        voteYesNoError.saveValue();
        noPermission.saveValue();
        requiredArgs.saveValue();
    }

    //todo: complete this
}
