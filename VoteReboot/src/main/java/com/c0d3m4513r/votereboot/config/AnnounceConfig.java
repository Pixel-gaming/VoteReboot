package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ListConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
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
    private ConfigEntry<Boolean> enableTimerChatAnnounce = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.chat");
    @NonNull
    private ConfigEntry<Boolean> enableTimerSoundAnnounce = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.sound");
    @NonNull
    private ConfigEntry<Boolean> enableScoreboard = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.scoreboard");
    @NonNull
    private ConfigEntry<Boolean> enableTitle = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.enabled.title");
    @NonNull
    private ConfigEntry<Boolean> soundAnnouncePlayGlobal = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            , "votereboot.announceRestarts.sound.playGlobal");
    @NonNull
    private ConfigEntry<String> soundId = new ConfigEntry<>(new ClassValue<>("block.note.pling", String.class)
            , "votereboot.announceRestarts.sound.id");
    @NonNull
    private ConfigEntry<Integer> soundAnnounceVolume = new ConfigEntry<>(new ClassValue<>(4000, Integer.class)
            , "votereboot.announceRestarts.sound.volume");
    @NonNull
    private ListConfigEntry<String> timerAnnounceAt = new ListConfigEntry<>(
            new ClassValue<>(
                    Arrays.asList("10m", "5m", "2m", "1m", "30s", "20s", "10s", "5s", "4s", "3s", "2s", "1s"),
                    String.class
            ), "votereboot.announceRestarts.at");

    @Override
    public void loadValue() {
        enableTimerChatAnnounce.loadValue();
        enableScoreboard.loadValue();
        enableTitle.loadValue();
        timerAnnounceAt.loadValue();
        soundAnnouncePlayGlobal.loadValue();
        soundAnnounceVolume.loadValue();
    }

    @Override
    public void saveValue() {
        enableTimerChatAnnounce.saveValue();
        enableScoreboard.saveValue();
        enableTitle.saveValue();
        timerAnnounceAt.saveValue();
        soundAnnouncePlayGlobal.saveValue();
        soundAnnounceVolume.saveValue();
    }
}
