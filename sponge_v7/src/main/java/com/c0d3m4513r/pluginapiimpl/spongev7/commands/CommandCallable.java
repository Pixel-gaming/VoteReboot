package com.c0d3m4513r.pluginapiimpl.spongev7.commands;

import com.c0d3m4513r.pluginapi.command.Command;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommandCallable implements org.spongepowered.api.command.CommandCallable {
    @NonNull
    @Getter
    Command command;

    @Override
    public org.spongepowered.api.command.CommandResult process(CommandSource source, String arguments) throws CommandException {
        try {
            return ((CommandResult)
                    command.process(
                            new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source),
                            arguments)
            ).getComandResult();
        } catch (com.c0d3m4513r.pluginapi.command.CommandException ce) {
            throw new CommandException(Text.of(ce.getMessage()), ce.getE());
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        try {
            return command.getSuggestions(
                    new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source),
                    arguments);
        } catch (com.c0d3m4513r.pluginapi.command.CommandException ce) {
            throw new CommandException(Text.of(ce.getMessage()), ce.getE());
        }
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return command.testPermission(
                new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source)
        );
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        Optional<String> ostr = command.getShortDescription(
                new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source)
        );
        return ostr.map(Text::of);
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        Optional<String> ostr = command.getHelp(
                new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source)
        );
        return ostr.map(Text::of);
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of(command.getUsage(
                new com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandSource(source)
        ));
    }
}
