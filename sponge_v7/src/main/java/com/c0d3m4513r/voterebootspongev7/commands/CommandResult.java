package com.c0d3m4513r.voterebootspongev7.commands;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandResult implements com.c0d3m4513r.voterebootapi.command.CommandResult {
    @NonNull
    org.spongepowered.api.command.CommandResult commandResult;
}
