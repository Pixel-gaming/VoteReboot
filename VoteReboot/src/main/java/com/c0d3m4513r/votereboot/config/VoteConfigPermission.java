package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.voterebootapi.config.ClassValue;
import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfigPermission implements IConfigLoadableSaveable {
    public static VoteConfigPermission getInstance(){
        return VoteConfig.getInstance().getVoteConfigPermission();
    }
    @NonNull
    private  ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot",String.class),
            "votereboot.vote.permission.reboot");
    @NonNull
    private  ConfigEntry<String> reload = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.reload",String.class),
            "votereboot.vote.permission.reload");
    @NonNull
    private ActionConfig voteAction = new ActionConfig(
            "votereboot.reboot.vote"
            ,"votereboot.vote.permission.vote");
    @NonNull
    private ActionConfig manualAction = new ActionConfig("votereboot.reboot.manual","votereboot.vote.permission.manual");
    @NonNull
    private ActionConfig scheduledAction = new ActionConfig("votereboot.reboot.scheduled","votereboot.vote.permission.scheduled");
    @NonNull
    private  ConfigEntry<String> voteRegister = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.vote.vote",String.class),
            "votereboot.vote.permission.registervote");


    @Override
    public void loadValue() {
        rebootCommand.loadValue();
        reload.loadValue();
        voteAction.loadValue();
        manualAction.loadValue();
        scheduledAction.loadValue();
        voteRegister.loadValue();
    }

    @Override
    public void saveValue() {
        rebootCommand.saveValue();
        reload.saveValue();
        voteAction.saveValue();
        manualAction.saveValue();
        scheduledAction.saveValue();
        voteRegister.saveValue();
    }
}
