package com.c0d3m4513r.voterebootspongev7.commands;

import com.c0d3m4513r.voterebootapi.command.Command;
import com.c0d3m4513r.voterebootspongev7.Plugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommandRegistrar implements com.c0d3m4513r.voterebootapi.command.CommandRegistrar {
    @NonNull
    Plugin plugin;

    @Override
    public Optional<com.c0d3m4513r.voterebootapi.command.CommandMapping> register(Command command, List<String> alias) {
        return Sponge.getCommandManager().register((Object) plugin, (CommandCallable) command, alias)
                .map(CommandMapping::new);
    }
}
