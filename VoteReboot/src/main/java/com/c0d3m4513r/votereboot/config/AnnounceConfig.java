package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ListConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.config.iface.Loadable;
import com.c0d3m4513r.pluginapi.config.iface.Savable;
import lombok.*;

import java.util.Arrays;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class AnnounceConfig implements IConfigLoadableSaveable {
    public static AnnounceConfig getInstance(){
        return Config.announceConfig;
    }
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Boolean> enableTimerChatAnnounce = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.chat");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Boolean> enableTimerSoundAnnounce = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.sound");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Boolean> enableScoreboard = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.scoreboard");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Boolean> enableTitle = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.title");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Boolean> soundAnnouncePlayGlobal = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.sound.playGlobal");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> soundId = new ConfigEntry<>(new ClassValue<>("block.note.pling", String.class)
            , "votereboot.announceRestarts.sound.id");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<Integer> soundAnnounceVolume = new ConfigEntry<>(new ClassValue<>(4000, Integer.class)
            , "votereboot.announceRestarts.sound.volume");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> scoreboardTitle = new ConfigEntry<>(new ClassValue<>("Restart Vote", String.class)
            , "votereboot.announceRestarts.scoreboard.title");
    @NonNull
    @Loadable
    @Savable
    private ListConfigEntry<String> timerAnnounceAt = new ListConfigEntry<>(
            new ClassValue<>(
                    Arrays.asList("10m", "5m", "2m", "1m", "30s", "20s", "10s", "5s", "4s", "3s", "2s", "1s"),
                    String.class
            ), "votereboot.announceRestarts.at");

}
