package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.config.iface.RSMCConfig;
import lombok.Getter;
//import org.bukkit.configuration.file.FileConfiguration;

public class VoteConfigPermission {
    @Getter
    private static ConfigEntry<String> rebootCommand = new ConfigEntry<>(
            "votereboot.reboot",
            "votereboot.vote.permission.reboot");
    @Getter
    private static ConfigEntry<String> reload = new ConfigEntry<>(
            "votereboot.reboot.reload",
            "votereboot.vote.permission.reload");
    @Getter
    private static ConfigEntry<String> cancel = new ConfigEntry<>(
            "votereboot.reboot.cancel",
            "votereboot.vote.permission.cancel");
    @Getter
    private static RSMCConfig voteAction = new RSMCConfig("votereboot.reboot.vote","votereboot.vote.permission.vote");
    @Getter
    private static RSMCConfig manualAction = new RSMCConfig("votereboot.reboot.manual","votereboot.vote.permission.manual");
    @Getter
    private static RSMCConfig scheduledAction = new RSMCConfig("votereboot.reboot.scheduled","votereboot.vote.permission.scheduled");
    @Getter
    private static ConfigEntry<String> voteRegister = new ConfigEntry<>(
            "votereboot.reboot.vote.vote",
            "votereboot.vote.permission.registervote");
    @Getter
    private static ConfigEntry<String> voteNoPermissionTemplate = new ConfigEntry<>(
            "You do not have permission to vote %answer%.",
            "votereboot.vote.permission.voteNoPermission");


}
