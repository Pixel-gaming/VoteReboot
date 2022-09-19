package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class VoteConfigPermission implements IConfigLoadableSaveable {
    private  ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            "votereboot.reboot",
            "votereboot.vote.permission.reboot");
    private  ConfigEntry<String> reload = new ConfigEntry<>(
            "votereboot.reboot.reload",
            "votereboot.vote.permission.reload");
    private ActionConfig voteAction = new ActionConfig("votereboot.reboot.vote","votereboot.vote.permission.vote");
    private ActionConfig manualAction = new ActionConfig("votereboot.reboot.manual","votereboot.vote.permission.manual");
    private ActionConfig scheduledAction = new ActionConfig("votereboot.reboot.scheduled","votereboot.vote.permission.scheduled");
    private  ConfigEntry<String> voteRegister = new ConfigEntry<>(
            "votereboot.reboot.vote.vote",
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
