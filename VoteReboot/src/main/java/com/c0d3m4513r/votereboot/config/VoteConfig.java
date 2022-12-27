package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ListConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.config.iface.Loadable;
import com.c0d3m4513r.pluginapi.config.iface.Savable;
import lombok.*;

import java.util.Arrays;


@Data
@Setter(AccessLevel.NONE)
public class VoteConfig implements IConfigLoadableSaveable {
    public static VoteConfig getInstance(){
        return Config.voteConfig;
    }

    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<Double> percentToRestart = new ConfigEntry<>(new ClassValue<>(100.0,Double.class),"votereboot.vote.percentToRestart");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<Integer> minAgree = new ConfigEntry<>(new ClassValue<>(Integer.MAX_VALUE,Integer.class),"votereboot.vote.minAgree");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<Integer> votingTime = new ConfigEntry<>(new ClassValue<>(100,Integer.class),"votereboot.vote.votingTime");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<Integer> votingRestartTime = new ConfigEntry<>(new ClassValue<>(30,Integer.class),"votereboot.vote.votingRestartTime");
    @NonNull
    @Loadable
    @Savable
    private ListConfigEntry<String> yesList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("yes", "ye", "y","t","true"), String.class)
            ,"votereboot.vote.yesList");
    @NonNull
    @Loadable
    @Savable
    private  ListConfigEntry<String> noList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("no", "n","f","false"),String.class)
            ,"votereboot.vote.noList");
    @NonNull
    @Loadable
    @Savable
    private  ListConfigEntry<String> noneList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("null", "none"),String.class)
            ,"votereboot.vote.noneList");
}
