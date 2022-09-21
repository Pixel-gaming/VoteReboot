package com.c0d3m4513r.pluginapiimpl.spongev7.commands;

import com.c0d3m4513r.pluginapi.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.Set;

@RequiredArgsConstructor
public class CommandMapping implements com.c0d3m4513r.pluginapi.command.CommandMapping {
    @NonNull
    @Getter(AccessLevel.PACKAGE)
    org.spongepowered.api.command.CommandMapping commandMapping;
    @Override
    public String getPrimaryAlias() {
        return commandMapping.getPrimaryAlias();
    }

    @Override
    public Set<String> getAllAliases() {
        return commandMapping.getAllAliases();
    }

    @Override
    public Command getCallable() {
        return (Command) commandMapping.getCallable();
    }
}
