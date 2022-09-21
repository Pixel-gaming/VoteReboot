package com.c0d3m4513r.pluginapiimpl.spongev7.commands;

import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapiimpl.spongev7.Plugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommandRegistrar implements com.c0d3m4513r.pluginapi.command.CommandRegistrar {
    @NonNull
    Plugin plugin;

    @Override
    public Optional<com.c0d3m4513r.pluginapi.command.CommandMapping> register(Command command, List<String> alias) {
        plugin.getLogger().info("Trying to register Command with Alias'"+alias.toString()+"'.");
        Optional<org.spongepowered.api.command.CommandMapping> cm =
                Sponge.getCommandManager().register(plugin, new CommandCallable(command), alias);
        if (cm.isPresent()){
            plugin.getLogger().info("Registered the Following Aliases'"+cm.get().getAllAliases().toString()+"'.");
            return Optional.of(new CommandMapping(cm.get()));
        }else{
            plugin.getLogger().info("Trying to register Command with Alias'"+alias.toString()+"'.");
            return Optional.empty();
        }


    }
}
