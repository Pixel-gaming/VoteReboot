package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfig implements IConfigLoadableSaveable {
    public  VoteConfigPermission voteConfigPermission;
    public  VoteConfigStrings voteConfigStrings;
    private  ConfigEntry<Double> percentToRestart = new ConfigEntry<>(100.0,"votereboot.vote.percentToRestart");
    private  ConfigEntry<Integer> minAgree = new ConfigEntry<>(Integer.MAX_VALUE,"votereboot.vote.minAgree");
    private  ConfigEntry<Integer> votingTime = new ConfigEntry<>(100,"votereboot.vote.votingTime");

    private  ConfigEntry<List<String>> yesList = new ConfigEntry<>(Arrays.asList("yes", "ye", "y","t","true"),
            "votereboot.vote.yesList");

    private  ConfigEntry<List<String>> noList = new ConfigEntry<>(Arrays.asList("no", "n","f","false"),
            "votereboot.vote.noList");
    private  ConfigEntry<List<String>> noneList = new ConfigEntry<>(Arrays.asList("null", "none"),
            "votereboot.vote.noneList");
    private  ConfigEntry<Boolean> kickEnabled = new ConfigEntry<>(true, "votereboot.vote.kick.enabled");
    private  ConfigEntry<Boolean> useCustomMessage = new ConfigEntry<>(false, "votereboot.vote.kick.useCustomMessage");

    private  ConfigEntry<String> customMessage = new ConfigEntry<>(
            "The Server is Restarting!",
            "votereboot.vote.kick.customMessage");
    private  ConfigEntry<Boolean> actionsEnabled = new ConfigEntry<>(true,"votereboot.vote.actions.enabled");
    private  ConfigEntry<String[]> rebootCommands = new ConfigEntry<>(new String[]{"save-all"},"votereboot.vote.actions.commands");

    @Override
    public void loadValue() {
        voteConfigPermission.loadValue();
        voteConfigStrings.loadValue();
        percentToRestart.loadValue();
        minAgree.loadValue();
        votingTime.loadValue();
        yesList.loadValue();
        noList.loadValue();
        noneList.loadValue();
        kickEnabled.loadValue();
        useCustomMessage.loadValue();
        customMessage.loadValue();
        actionsEnabled.loadValue();
        rebootCommands.loadValue();
    }

    @Override
    public void saveValue() {
        voteConfigPermission.saveValue();
        voteConfigStrings.saveValue();
        percentToRestart.saveValue();
        minAgree.saveValue();
        votingTime.saveValue();
        yesList.saveValue();
        noList.saveValue();
        noneList.saveValue();
        kickEnabled.saveValue();
        useCustomMessage.saveValue();
        customMessage.saveValue();
        actionsEnabled.saveValue();
        rebootCommands.saveValue();
    }
}
