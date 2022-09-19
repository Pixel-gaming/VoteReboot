package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.voterebootapi.command.Command;
import com.c0d3m4513r.voterebootapi.command.CommandException;
import com.c0d3m4513r.voterebootapi.command.CommandResult;
import com.c0d3m4513r.voterebootapi.command.CommandSource;

import java.util.List;
import java.util.Optional;

public class Reboot implements Command {

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return Command.super.testPermission(source);
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return null;
    }
}
