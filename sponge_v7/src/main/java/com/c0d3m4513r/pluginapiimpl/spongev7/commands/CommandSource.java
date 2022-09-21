package com.c0d3m4513r.pluginapiimpl.spongev7.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.text.Text;

@RequiredArgsConstructor
public class CommandSource implements com.c0d3m4513r.pluginapi.command.CommandSource {
    @NonNull
    @Getter(AccessLevel.PACKAGE)
    org.spongepowered.api.command.CommandSource commandSource;

    @Override
    public boolean hasPerm(String perm) {
        return commandSource.hasPermission(perm);
    }

    @Override
    public void sendMessage(@NonNull String message) {
        commandSource.sendMessage(Text.of(message));
    }
}
