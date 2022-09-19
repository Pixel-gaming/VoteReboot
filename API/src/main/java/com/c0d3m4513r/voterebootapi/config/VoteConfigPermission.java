package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.config.iface.RSMCConfig;
import lombok.Getter;
//import org.bukkit.configuration.file.FileConfiguration;

public class VoteConfigPermission {
    @Getter
    private static ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            "votereboot.reboot",
            "votereboot.reboot.permission.reboot");
    @Getter
    private static ConfigEntry<String> reload = new ConfigEntry<>(
            "votereboot.reboot.reload",
            "votereboot.reboot.permission.reload");
    @Getter
    private static ConfigEntry<String> cancel = new ConfigEntry<>(
            "votereboot.reboot.cancel",
            "votereboot.reboot.permission.cancel");
    @Getter
    private static RSMCConfig voteAction = new RSMCConfig("votereboot.reboot.vote","votereboot.reboot.permission.vote");
    @Getter
    private static RSMCConfig manualAction = new RSMCConfig("votereboot.reboot.manual","votereboot.reboot.permission.manual");
    @Getter
    private static RSMCConfig scheduledAction = new RSMCConfig("votereboot.reboot.scheduled","votereboot.reboot.permission.scheduled");
    @Getter
    private static ConfigEntry<String> voteRegister = new ConfigEntry<>(
            "votereboot.reboot.vote.vote",
            "votereboot.reboot.permission.registervote");
    @Getter
    private static ConfigEntry<String> voteNoPermissionTemplate = new ConfigEntry<>(
            "You do not have permission to vote %answer%.",
            "votereboot.reboot.permission.voteNoPermission");


}
