package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.pluginapi.config.*;
import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ListConfigEntry;
import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import com.c0d3m4513r.votereboot.commands.RebootSubcommands;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import com.c0d3m4513r.votereboot.reboot.ScheduledAction;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    public static final boolean DEBUG = true;
    @NonNull
    public static final VoteConfig voteConfig = new VoteConfig();
    @NonNull
    public static final ConfigPermission configPermission = new ConfigPermission();
    @NonNull
    public static final AnnounceConfig announceConfig = new AnnounceConfig();
    public static final Map<String, RestartType> restartTypeConversion = new HashMap<String, RestartType>() {
        {
            put("Vote", RestartType.Vote);
            put("vote", RestartType.Vote);
            put("v", RestartType.Vote);
            put("manual", RestartType.Manual);
            put("m", RestartType.Manual);
            put("Manual", RestartType.Manual);
            put("Scheduled", RestartType.Scheduled);
            put("scheduled", RestartType.Scheduled);
            put("s", RestartType.Scheduled);
            put("All", RestartType.All);
            put("all", RestartType.All);
            put("a", RestartType.All);
        }
    };
    //DON'T move this above ConfigPermission init. That will cause issues.
    public static final Map<String, RebootSubcommands> subcommandConversion = new HashMap<String, RebootSubcommands>() {
        {
            put("help", RebootSubcommands.help);
            put("h", RebootSubcommands.help);
            put("usage", RebootSubcommands.usage);
            put("u", RebootSubcommands.usage);
            put("vote", RebootSubcommands.vote);
            put("v", RebootSubcommands.vote);
            put("start", RebootSubcommands.start);
            put("s", RebootSubcommands.start);
            put("now", RebootSubcommands.now);
            put("n", RebootSubcommands.now);
            put("cancel", RebootSubcommands.cancel);
            put("c", RebootSubcommands.cancel);
            put("time", RebootSubcommands.time);
            put("t", RebootSubcommands.time);
            put("reloadConfig", RebootSubcommands.reloadConfig);
            put("reload", RebootSubcommands.reloadConfig);
            put("r", RebootSubcommands.reloadConfig);
        }
    };
    @NonNull
    private ListConfigEntry<String> scheduledRestarts = new ListConfigEntry<>(new ClassValue<>(Arrays.asList("3h", "3h+30m", "4h"), String.class)
            , "votereboot.scheduledRestarts");

    @NonNull
    private ListConfigEntry<String> aliasList = new ListConfigEntry<>(
            new ClassValue<>(Arrays.asList("reboot", "restart"), String.class)
            , "votereboot.aliasList");

    @NonNull
    private ConfigEntry<Boolean> kickEnabled = new ConfigEntry<>(new ClassValue<>(true, Boolean.class), "votereboot.kick.enabled");
    @NonNull
    private ConfigEntry<Boolean> useCustomMessage = new ConfigEntry<>(new ClassValue<>(false, Boolean.class), "votereboot.kick.useCustomMessage");
    @NonNull
    private ConfigEntry<String> customMessage = new ConfigEntry<>(new ClassValue<>(
            "The Server is Restarting!", String.class),
            "votereboot.kick.customMessage");
    @NonNull
    private ConfigEntry<Boolean> actionsEnabled = new ConfigEntry<>(new ClassValue<>(true, Boolean.class), "votereboot.actions.enabled");
    @NonNull
    private ListConfigEntry<String> rebootCommands = new ListConfigEntry<>(
            new ClassValue<>(Collections.singletonList("save-all"), String.class)
            , "votereboot.actions.commands");

    @NonNull
    public ConfigStrings configStrings = new ConfigStrings();
    @NonNull
    public ConfigCommandStrings configCommandStrings = new ConfigCommandStrings();

    public static Config getInstance() {
        return ((Config) API.getConfig());
    }

    @Override
    public void loadValue() {
        getLogger().info("[VoteReboot] Load Config");
        voteConfig.loadValue();
        configPermission.loadValue();
        announceConfig.loadValue();
        scheduledRestarts.loadValue();
        aliasList.loadValue();
        kickEnabled.loadValue();
        useCustomMessage.loadValue();
        customMessage.loadValue();
        actionsEnabled.loadValue();
        rebootCommands.loadValue();
        configStrings.loadValue();
        configCommandStrings.loadValue();
        getLogger().info("[VoteReboot] Load Config Done");
    }

    @Override
    public void saveValue() {
        getLogger().info("[VoteReboot] Save Config");
        voteConfig.saveValue();
        configPermission.saveValue();
        announceConfig.saveValue();
        scheduledRestarts.saveValue();
        aliasList.saveValue();
        kickEnabled.saveValue();
        useCustomMessage.saveValue();
        customMessage.saveValue();
        actionsEnabled.saveValue();
        rebootCommands.saveValue();
        configStrings.saveValue();
        configCommandStrings.saveValue();
        getLogger().info("[VoteReboot] Save Config Done");
    }

    @Override
    public void main() {
        getLogger().info("[VoteReboot] Registering Command Register Hook");
        new EventRegistrar(this::registerScheduledReboots, EventType.preinit, 0);
        new EventRegistrar(this::registerCommands, EventType.commandRegister, 0);
        new EventRegistrar(() -> {
            //There might be config changes then
            if (getActionsEnabled().getValue()) {
                //do this explicitly sync, because I don't want any nonsense Stream async/Concurrency.
                for (val val:getRebootCommands().getValue())
                        API.getServer().execCommand(val);
            }
        }, EventType.onReboot, Integer.MAX_VALUE);
    }

    @Override
    public Optional<String> getDefaultConfigContents() {
        try (
//                URLClassLoader loader = URLClassLoader.newInstance(new URL[]{Config.class.getProtectionDomain().getCodeSource().getLocation()});
                InputStream is = Objects.requireNonNull(Config.class.getResourceAsStream("/config.yml"));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return Optional.of(br.lines().collect(Collectors.joining("\n")));
        } catch (NullPointerException | IOException e) {
            //We have no default config?
            getLogger().error("[VoteReboot] Exception occurred whilst trying to read default config", e);
            return Optional.empty();
        }
    }

    private void registerCommands() {
        getLogger().info("[VoteReboot] Register Commands");
        API.getCommandRegistrar().register(new Reboot(), getAliasList().getValue());
        getLogger().info("[VoteReboot] Register Commands Done");
    }

    private void registerScheduledReboots() {
        for (val e : getScheduledRestarts().getValue()) {
            Optional<TimeEntry> teo = TimeEntry.of(e);
            if (teo.isPresent()) {
                TimeUnitValue tu = teo.get().getMaxUnit();
                new ScheduledAction(tu);
            }
        }
    }
}
