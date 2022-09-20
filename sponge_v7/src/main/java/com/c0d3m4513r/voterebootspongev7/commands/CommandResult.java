package com.c0d3m4513r.voterebootspongev7.commands;

import com.c0d3m4513r.voterebootapi.Nullable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CommandResult implements com.c0d3m4513r.voterebootapi.command.CommandResult{
    boolean success = false;
    @Override
    public CommandResult success() {
        return new CommandResult(true);
    }

    @Override
    public CommandResult error() {
        return new CommandResult(false);
    }
    public org.spongepowered.api.command.CommandResult getComandResult(){
        if (success) return org.spongepowered.api.command.CommandResult.success();
        else return org.spongepowered.api.command.CommandResult.empty();
    }
}
