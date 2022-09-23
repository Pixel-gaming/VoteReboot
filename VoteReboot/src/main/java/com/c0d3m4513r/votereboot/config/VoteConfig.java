package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfig implements IConfigLoadableSaveable {
    public static VoteConfig getInstance(){
        return Config.voteConfig;
    }
    public static final Class<String[]> stringlist = String[].class;

    @NonNull
    private  ConfigEntry<Double> percentToRestart = new ConfigEntry<>(new ClassValue<>(100.0,Double.class),"votereboot.vote.percentToRestart");
    @NonNull
    private  ConfigEntry<Integer> minAgree = new ConfigEntry<>(new ClassValue<>(Integer.MAX_VALUE,Integer.class),"votereboot.vote.minAgree");
    @NonNull
    private  ConfigEntry<Integer> votingTime = new ConfigEntry<>(new ClassValue<>(100,Integer.class),"votereboot.vote.votingTime");
    @NonNull
    private  ConfigEntry<Integer> votingRestartTime = new ConfigEntry<>(new ClassValue<>(30,Integer.class),"votereboot.vote.votingRestartTime");
    @NonNull
    private  ConfigEntry<String[]> yesList = new ConfigEntry<>(new ClassValue<>(new String[]{"yes", "ye", "y","t","true"}, stringlist)
            ,"votereboot.vote.yesList");
    @NonNull
    private  ConfigEntry<String[]> noList = new ConfigEntry<>(new ClassValue<>(new String[]{"no", "n","f","false"},stringlist)
            ,"votereboot.vote.noList");
    @NonNull
    private  ConfigEntry<String[]> noneList = new ConfigEntry<>(new ClassValue<>(new String[]{"null", "none"},stringlist)
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
