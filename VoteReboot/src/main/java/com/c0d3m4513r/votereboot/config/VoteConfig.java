package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ListConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

import java.util.Arrays;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfig implements IConfigLoadableSaveable {
    public static VoteConfig getInstance(){
        return Config.voteConfig;
    }

    @NonNull
    private  ConfigEntry<Double> percentToRestart = new ConfigEntry<>(new ClassValue<>(100.0,Double.class),"votereboot.vote.percentToRestart");
    @NonNull
    private  ConfigEntry<Integer> minAgree = new ConfigEntry<>(new ClassValue<>(Integer.MAX_VALUE,Integer.class),"votereboot.vote.minAgree");
    @NonNull
    private  ConfigEntry<Integer> votingTime = new ConfigEntry<>(new ClassValue<>(100,Integer.class),"votereboot.vote.votingTime");
    @NonNull
    private  ConfigEntry<Integer> votingRestartTime = new ConfigEntry<>(new ClassValue<>(30,Integer.class),"votereboot.vote.votingRestartTime");
    @NonNull
    private ListConfigEntry<String> yesList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("yes", "ye", "y","t","true"), String.class)
            ,"votereboot.vote.yesList");
    @NonNull
    private  ListConfigEntry<String> noList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("no", "n","f","false"),String.class)
            ,"votereboot.vote.noList");
    @NonNull
    private  ListConfigEntry<String> noneList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("null", "none"),String.class)
            ,"votereboot.vote.noneList");
    @Override
    public void loadValue() {
        percentToRestart.loadValue();
        minAgree.loadValue();
        votingTime.loadValue();
        votingRestartTime.loadValue();
        yesList.loadValue();
        noList.loadValue();
        noneList.loadValue();
    }

    @Override
    public void saveValue() {
        percentToRestart.saveValue();
        minAgree.saveValue();
        votingTime.saveValue();
        votingRestartTime.saveValue();
        yesList.saveValue();
        noList.saveValue();
        noneList.saveValue();

    }
}
