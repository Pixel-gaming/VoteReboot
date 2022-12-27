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
public class ShortDescription implements IConfigLoadableSaveable {
    public static ShortDescription getInstance(){
        return ConfigTranslate.getInstance().getShortDescription();
    }
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionTime = new ConfigEntry<>(
            new ClassValue<>("'reboot time': View all timers, and their remaining time", String.class),
            "votereboot.translate.shortDescription.time");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionVote = new ConfigEntry<>(
            new ClassValue<>("'reboot vote': You can vote yes, no or abstain to restart the server", String.class),
            "votereboot.translate.shortDescription.vote");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionStart = new ConfigEntry<>(
            new ClassValue<>("'reboot start': Launches Other Actions, or manually sets a time after which the server will restart.", String.class),
            "votereboot.translate.shortDescription.start");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionCancel = new ConfigEntry<>(
            new ClassValue<>("'reboot cancel': Cancels a currently active Reboot timer.", String.class),
            "votereboot.translate.shortDescription.cancel");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionNow = new ConfigEntry<>(
            new ClassValue<>("'reboot now': Restart the server Instantly", String.class),
            "votereboot.translate.shortDescription.now");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionReload = new ConfigEntry<>(
            new ClassValue<>("'reboot reload': Reloads the configs for the VoteReboot plugin", String.class),
            "votereboot.translate.shortDescription.reload");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> ShortDescriptionHelp = new ConfigEntry<>(
            new ClassValue<>("'reboot help': Gets the help for the Reboot command.", String.class),
            "votereboot.translate.shortDescription.reload");

}
