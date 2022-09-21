package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class RestartTypeActionConfig implements IConfigLoadableSaveable {
    private final ActionConfig[] actionConfigs;

    public RestartTypeActionConfig(String permRoot, String configRoot) {
        actionConfigs = new ActionConfig[(int) RestartType.getMaxId()];
        for (val type : RestartType.values()) {
            actionConfigs[(int) type.id] = new ActionConfig(permRoot + "." + RestartType.asString(type), configRoot + "." + RestartType.asString(type));
        }
    }
    public RestartTypeActionConfig(String configRoot, String read, String start, String modify, String cancel) {
        actionConfigs = new ActionConfig[(int) RestartType.getMaxId()];
        for (val type : RestartType.values()) {
            actionConfigs[(int) type.id] = new ActionConfig(read,start,modify,cancel, configRoot + "." + RestartType.asString(type));
        }
    }
    //what an abomination.... Help
    public RestartTypeActionConfig(
           String voteRead, String voteStart, String voteModify, String voteCancel,
           String manualRead, String manualStart, String manualModify, String manualCancel,
           String scheduledRead, String scheduledStart, String scheduledModify, String scheduledCancel,
           String allRead, String allStart, String allModify, String allCancel,
           String configRoot
    ) {
        actionConfigs = new ActionConfig[(int) RestartType.getMaxId()];
        actionConfigs[(int)RestartType.Vote.id] = new ActionConfig(voteRead,voteStart,voteModify,voteCancel,configRoot+"."+RestartType.asString(RestartType.Vote));
        actionConfigs[(int)RestartType.ManualRestart.id] = new ActionConfig(manualRead,manualStart,manualModify,manualCancel,configRoot+"."+RestartType.asString(RestartType.ManualRestart));
        actionConfigs[(int)RestartType.Scheduled.id] = new ActionConfig(scheduledRead,scheduledStart,scheduledModify,scheduledCancel,configRoot+"."+RestartType.asString(RestartType.Scheduled));
        actionConfigs[(int)RestartType.All.id] = new ActionConfig(allRead,allStart,allModify,allCancel,configRoot+"."+RestartType.asString(RestartType.All));
    }

    public @NonNull ActionConfig getAction(@NonNull RestartType type){
        return actionConfigs[(int) type.id];
    }

    @Override
    public void loadValue() {
        for(val action:actionConfigs) {
            action.loadValue();
        }
    }

    @Override
    public void saveValue() {
        for(val action:actionConfigs){
            action.saveValue();
        }
    }
}
