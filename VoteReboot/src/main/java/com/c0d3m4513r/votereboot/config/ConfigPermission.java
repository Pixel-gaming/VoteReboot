package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ConfigPermission implements IConfigLoadableSaveable {
    public static ConfigPermission getInstance(){
        return Config.configPermission;
    }
    @NonNull
    private  ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot",String.class),
            "votereboot.permission.rebootCommand");
    @NonNull
    private  ConfigEntry<String> reload = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.reload",String.class),
            "votereboot.permission.reload");
    @NonNull
    private  ConfigEntry<String> voteRegister = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.vote",String.class),
            "votereboot.permission.registervote");
    @NonNull
    private RestartTypeActionConfig restartTypeAction = new RestartTypeActionConfig("votereboot.reboot.type","votereboot.permission.type");

    @Override
    public void loadValue() {
        rebootCommand.loadValue();
        reload.loadValue();
        restartTypeAction.loadValue();
        voteRegister.loadValue();
    }

    @Override
    public void saveValue() {
        rebootCommand.saveValue();
        reload.saveValue();
        restartTypeAction.saveValue();
        voteRegister.saveValue();
    }
}
