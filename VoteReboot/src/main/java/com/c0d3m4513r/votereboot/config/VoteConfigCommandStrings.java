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
public class VoteConfigCommandStrings implements IConfigLoadableSaveable {
    public static VoteConfigCommandStrings getInstance() {
        return VoteConfig.getInstance().getVoteConfigCommandStrings();
    }

    @NonNull
    private ConfigEntry<String> shortDescription = new ConfigEntry<>(
            new ClassValue<>("Vote to Reboot the server", String.class),
            "votereboot.vote.translate.shortDescription");
    @NonNull
    private ConfigEntry<String> helpBase = new ConfigEntry<>(
            new ClassValue<>("This is the main command for managing server restarts.", String.class),
            "votereboot.vote.translate.help.rebootCommand");
    @NonNull
    private ConfigEntry<String> helpReload = new ConfigEntry<>(new ClassValue<>(
            " - reload This will reload all of the configs from disk again, and apply them."
            , String.class), "votereboot.vote.translate.help.reload");
    @NonNull
    private ConfigEntry<String> helpRegisterVote = new ConfigEntry<>(new ClassValue<>(
            " - vote [yes/no/null/none] This will add a vote, on weather the server should be restarted or not."
            , String.class), "votereboot.vote.translate.help.registervote");
    @NonNull
    private ConfigEntry<String> helpRead = new ConfigEntry<>(
            new ClassValue<>(" - time This will List all visible reboots, that are currently scheduled"
            , String.class), "votereboot.vote.translate.help.read");

    @NonNull
    private ActionConfig helpVote = new ActionConfig(
            "",
            "Insert help about the commands usage here.",
            "Not user callable yet!",
            "Insert help about the commands usage here.",
            "votereboot.vote.translate.help.vote");
    @NonNull
    private ActionConfig helpManual = new ActionConfig(
            "",
            "Insert help about the commands usage here.",
            "Not user callable yet!",
            "Insert help about the commands usage here.",
            "votereboot.vote.translate.help.manual");
    @NonNull
    private ActionConfig helpScheduled = new ActionConfig(
            "",
            "Insert help about the commands usage here.",
            "Not user callable yet!",
            "Insert help about the commands usage here.",
            "votereboot.vote.translate.help.scheduled");

    @Override
    public void loadValue() {
        shortDescription.loadValue();
        helpBase.loadValue();
        helpReload.loadValue();
        helpRegisterVote.loadValue();
        helpRead.loadValue();
        helpVote.loadValue();
        helpManual.loadValue();
        helpScheduled.loadValue();
    }

    @Override
    public void saveValue() {
        shortDescription.saveValue();
        helpBase.saveValue();
        helpReload.saveValue();
        helpRegisterVote.saveValue();
        helpRead.saveValue();
        helpVote.saveValue();
        helpManual.saveValue();
        helpScheduled.saveValue();
    }
}
