package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.voterebootapi.config.ClassValue;
import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import io.leangen.geantyref.TypeToken;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import java.util.Arrays;
import java.util.List;
@Data
@Setter(AccessLevel.NONE)
public class VoteConfig implements IConfigLoadableSaveable {
    public static final Class<String[]> stringlist = String[].class;

    @NonNull
    public  VoteConfigPermission voteConfigPermission=new VoteConfigPermission();
    @NonNull
    public  VoteConfigStrings voteConfigStrings=new VoteConfigStrings();
    @NonNull
    private ConfigEntry<String[]> aliasList = new ConfigEntry<>(new ClassValue<>(new String[]{"reboot", "restart"}, stringlist)
            ,"votereboot.vote.aliasList");
    @NonNull
    private  ConfigEntry<Double> percentToRestart = new ConfigEntry<>(new ClassValue<>(100.0,Double.class),"votereboot.vote.percentToRestart");
    @NonNull
    private  ConfigEntry<Integer> minAgree = new ConfigEntry<>(new ClassValue<>(Integer.MAX_VALUE,Integer.class),"votereboot.vote.minAgree");
    @NonNull
    private  ConfigEntry<Integer> votingTime = new ConfigEntry<>(new ClassValue<>(100,Integer.class),"votereboot.vote.votingTime");
    @NonNull
    private  ConfigEntry<String[]> yesList = new ConfigEntry<>(new ClassValue<>(new String[]{"yes", "ye", "y","t","true"}, stringlist)
            ,"votereboot.vote.yesList");
    @NonNull
    private  ConfigEntry<String[]> noList = new ConfigEntry<>(new ClassValue<>(new String[]{"no", "n","f","false"},stringlist)
            ,"votereboot.vote.noList");
    @NonNull
    private  ConfigEntry<String[]> noneList = new ConfigEntry<>(new ClassValue<>(new String[]{"null", "none"},stringlist)
            ,"votereboot.vote.noneList");
    @NonNull
    private  ConfigEntry<Boolean> kickEnabled = new ConfigEntry<>(new ClassValue<>(true,Boolean.class), "votereboot.vote.kick.enabled");
    @NonNull
    private  ConfigEntry<Boolean> useCustomMessage = new ConfigEntry<>(new ClassValue<>(false,Boolean.class), "votereboot.vote.kick.useCustomMessage");
    @NonNull
    private  ConfigEntry<String> customMessage = new ConfigEntry<>(new ClassValue<>(
            "The Server is Restarting!",String.class),
            "votereboot.vote.kick.customMessage");
    @NonNull
    private  ConfigEntry<Boolean> actionsEnabled = new ConfigEntry<>(new ClassValue<>(true,Boolean.class),"votereboot.vote.actions.enabled");
    @NonNull
    private  ConfigEntry<String[]> rebootCommands = new ConfigEntry<>(new ClassValue<>(new String[]{"save-all"},stringlist)
            ,"votereboot.vote.actions.commands");
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
