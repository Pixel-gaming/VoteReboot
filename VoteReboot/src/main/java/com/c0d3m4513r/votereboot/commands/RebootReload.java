package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.config.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootReload implements Command {
    public static final RebootReload INSTANCE = new RebootReload();
    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) {
        getLogger().info("[VoteReboot] Explicit Config load was requested. Loading configs in new async thread");
        try {
            API.runOnMain(()->{API.getConfigLoader().updateConfigLoader();
                Config.getInstance().loadValue();});
            //todo: Move the string to config
            source.sendMessage("Configs have been requested to reload in an async thread.");
        } catch (OutOfMemoryError oom){
            getLogger().error("[VoteReboot] Async Thread creation failed. Saving Config Synchronously Error is:",oom);
            API.getConfigLoader().updateConfigLoader();
            Config.getInstance().loadValue();
            //todo: Move the string to config
            source.sendMessage("Configs have been reloaded synchronously. There has been an error during thread creation. Please see in the console for more details.");
        }
        //todo: Move the string to config
        source.sendMessage("Configs have been loaded.");
        return API.getCommandResult().success();
    }

    //reload has no arguments
    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Reloads the configs for the VoteReboot plugin");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/reboot reload";
    }
}
