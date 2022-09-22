package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.pluginapi.config.*;
import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.convert.Convert;
import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import com.c0d3m4513r.votereboot.reboot.ScheduledAction;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;
import static com.c0d3m4513r.votereboot.config.VoteConfig.stringlist;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    public static final boolean DEBUG = false;
    public static final Map<String, RestartType> restartTypeConversion = new HashMap<String, RestartType>(){
        {
            put("Vote",RestartType.Vote);
            put("vote",RestartType.Vote);
            put("v",RestartType.Vote);
            put("manual",RestartType.Manual);
            put("m",RestartType.Manual);
            put("Manual",RestartType.Manual);
            put("Scheduled",RestartType.Scheduled);
            put("scheduled",RestartType.Scheduled);
            put("s",RestartType.Scheduled);
            put("All",RestartType.All);
            put("all",RestartType.All);
            put("a",RestartType.All);
        }
    };
    @NonNull
    private VoteConfig voteConfig = new VoteConfig();
    @NonNull
    public ConfigPermission configPermission =new ConfigPermission();
    @NonNull
    private ConfigEntry<String[]> scheduledRestarts = new ConfigEntry<>(new ClassValue<>(new String[]{"3,h","3,h+30,m","4,h"}, stringlist)
            ,"votereboot.scheduledRestarts");

    @NonNull
    private ConfigEntry<Boolean> enableTimerAnnounce = new ConfigEntry<>(new ClassValue<>(true, Boolean.class)
            ,"votereboot.announceRestarts.enabled");

    @NonNull
    private ConfigEntry<String[]> timerAnnounceAt = new ConfigEntry<>(new ClassValue<>(new String[]
    {"10,m","5,m","2,m","1,m","30,s","20,s","10,s","5,s","4,s","3,s","2,s","1,s"}, stringlist),
            "votereboot.announceRestarts.at");

    @NonNull
    private ConfigEntry<String[]> aliasList = new ConfigEntry<>(new ClassValue<>(new String[]{"reboot", "restart"}, stringlist)
            ,"votereboot.aliasList");

    @NonNull
    private  ConfigEntry<Boolean> kickEnabled = new ConfigEntry<>(new ClassValue<>(true,Boolean.class), "votereboot.kick.enabled");
    @NonNull
    private  ConfigEntry<Boolean> useCustomMessage = new ConfigEntry<>(new ClassValue<>(false,Boolean.class), "votereboot.kick.useCustomMessage");
    @NonNull
    private  ConfigEntry<String> customMessage = new ConfigEntry<>(new ClassValue<>(
            "The Server is Restarting!",String.class),
            "votereboot.kick.customMessage");
    @NonNull
    private  ConfigEntry<Boolean> actionsEnabled = new ConfigEntry<>(new ClassValue<>(true,Boolean.class),"votereboot.actions.enabled");
    @NonNull
    private  ConfigEntry<String[]> rebootCommands = new ConfigEntry<>(new ClassValue<>(new String[]{"save-all"},stringlist)
            ,"votereboot.actions.commands");

    @NonNull
    public ConfigStrings configStrings =new ConfigStrings();
    @NonNull
    public ConfigCommandStrings configCommandStrings =new ConfigCommandStrings();

    public static Config getInstance(){
        return ((Config)API.getConfig());
    }

    @Override
    public void loadValue() {
        getLogger().info("[VoteReboot] Load Config");
        voteConfig.loadValue();
        scheduledRestarts.loadValue();
        enableTimerAnnounce.loadValue();
        timerAnnounceAt.loadValue();
        aliasList.loadValue();
        kickEnabled.loadValue();
        useCustomMessage.loadValue();
        customMessage.loadValue();
        actionsEnabled.loadValue();
        rebootCommands.loadValue();
        configPermission.loadValue();
        configStrings.loadValue();
        configCommandStrings.loadValue();
        getLogger().info("[VoteReboot] Load Config Done");
    }

    @Override
    public void saveValue() {
        getLogger().info("[VoteReboot] Save Config");
        voteConfig.saveValue();
        scheduledRestarts.saveValue();
        enableTimerAnnounce.saveValue();
        timerAnnounceAt.saveValue();
        aliasList.saveValue();
        kickEnabled.saveValue();
        useCustomMessage.saveValue();
        customMessage.saveValue();
        actionsEnabled.saveValue();
        rebootCommands.saveValue();
        configPermission.saveValue();
        configStrings.saveValue();
        configCommandStrings.saveValue();
        getLogger().info("[VoteReboot] Save Config Done");
    }

    @Override
    public void main() {
        getLogger().info("[VoteReboot] Registering Command Register Hook");
        new EventRegistrar(this::registerScheduledReboots, EventType.preinit,0);
        new EventRegistrar(this::registerCommands, EventType.commandRegister,0);
        new EventRegistrar(()->{
            //There might be config changes then
            if (getActionsEnabled().getValue()){
                Arrays.stream(getRebootCommands().getValue())
                        .forEach(API.getServer()::execCommand);
            }
        },EventType.onReboot,Integer.MAX_VALUE);
    }

    @Override
    public Optional<String> getDefaultConfigContents() {
        try(
//                URLClassLoader loader = URLClassLoader.newInstance(new URL[]{Config.class.getProtectionDomain().getCodeSource().getLocation()});
                InputStream is = Objects.requireNonNull(Config.class.getResourceAsStream("/config.yml"));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            return Optional.of(br.lines().collect(Collectors.joining("\n")));
        }catch (NullPointerException | IOException e){
            //We have no default config?
            getLogger().error("[VoteReboot] Exception occurred whilst trying to read default config",e);
            return Optional.empty();
        }
    }

    private void registerCommands(){
        getLogger().info("[VoteReboot] Register Commands");
        API.getCommandRegistrar().register(new Reboot(), Arrays.asList(getAliasList().getValue()));
        getLogger().info("[VoteReboot] Register Commands Done");
    }
    private void registerScheduledReboots(){
        for (val e:getScheduledRestarts().getValue()){
            Optional<TimeEntry> teo = TimeEntry.of(e);
            if (teo.isPresent()){
                TimeUnitValue tu = teo.get().getMaxUnit();
                new ScheduledAction(tu.getValue(),tu.getUnit());
            }
        }
    }
}
