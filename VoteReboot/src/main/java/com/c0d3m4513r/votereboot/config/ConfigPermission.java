package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.config.iface.Loadable;
import com.c0d3m4513r.pluginapi.config.iface.Savable;
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
    @Loadable
    @Savable
    private  ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.main",String.class),
            "votereboot.permission.rebootCommand");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<String> reload = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.reload",String.class),
            "votereboot.permission.reload");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<String> voteRegister = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.vote",String.class),
            "votereboot.permission.registervote");
    @NonNull
    @Loadable
    @Savable
    private RestartTypeActionConfig restartTypeAction = new RestartTypeActionConfig("votereboot.reboot.type","votereboot.permission.type");
    @NonNull
    @Loadable
    @Savable
    private  ConfigEntry<String> viewOverridenTimers = new ConfigEntry<>(
            new ClassValue<>("votereboot.reboot.viewoverriden",String.class),
            "votereboot.permission.viewoverriden");

}
