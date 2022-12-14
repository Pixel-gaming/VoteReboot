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
public class ConfigTranslateCommandHelp implements IConfigLoadableSaveable {
    public static ConfigTranslateCommandHelp getInstance() {
        return Config.getInstance().getConfigCommandStrings();
    }

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> shortDescription = new ConfigEntry<>(
            new ClassValue<>("Vote to Reboot the server", String.class),
            "votereboot.translate.shortDescription");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> helpBase = new ConfigEntry<>(
            new ClassValue<>("This is the main command for managing server restarts.\n Arguments with <> are optional, whilst ones with [] are mandatory. \n - stands for the main command alias. By default /reboot or /restart", String.class),
            "votereboot.translate.help.rebootCommand");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> helpReload = new ConfigEntry<>(new ClassValue<>(
            " - reloadConfig This will reload all of the configs from disk again, and apply them."
            , String.class), "votereboot.translate.help.reload");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> helpRegisterVote = new ConfigEntry<>(new ClassValue<>(
            " - vote [yes/no/null/none] This will add a vote, on weather the server should be restarted or not."
            , String.class), "votereboot.translate.help.registervote");
    @NonNull
    @Loadable
    @Savable
    private RestartTypeActionConfig helpRestartTypeAction = new RestartTypeActionConfig(
            //vote
            "",
            "- vote start This will start a Vote, to restart the server",
            "",
            "",
            //manual
            "",
            "\"- start [TimeEntry] <reason>\" or \"- start [TimeUnit] [time:long] <reason>\" This will restart the server in the time specified by TimeEntry",
            "",
            "",
            //scheduled
            "",
            "Insert help about the commands usage here.",
            "",
            "",
            //all
            "",
            "Insert help about the commands usage here.",
            "",
            "",
            //config root
            "votereboot.translate.help.type");
    @NonNull
    @Loadable
    @Savable
    private ActionConfig helpGeneralAction = new ActionConfig(
            " - time <Action> This will List all visible reboots, that are currently scheduled",
            "",
            "Not user callable yet!",
            " - cancel [Action] <Timed> <limit:uint> This will cancel the last limit(or all, if unspecified), starting from <Timed>,  reboots of type Action",
            //config root
            "votereboot.translate.help.general"
    );
}
