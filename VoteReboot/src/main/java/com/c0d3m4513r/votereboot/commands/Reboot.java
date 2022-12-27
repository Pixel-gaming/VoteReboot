package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.config.ConfigTranslateCommandHelp;
import com.c0d3m4513r.votereboot.config.ConfigTranslate;
import lombok.NonNull;
import lombok.var;

import java.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reboot implements Command {
    public final static Reboot INSTANCE = new Reboot();
    static Function<CommandSource, BiFunction<String,String,Boolean>> sendHelp = (source)->(perm, str) -> {
        if (source.hasPerm(perm)) {
            if (!str.isEmpty()) source.sendMessage(str);
            return true;
        }else return false;
    };

    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) throws CommandException {
        //arg0 should just be the command alias
        if (arguments.length > 0 && arguments[0] != null) {
            RebootSubcommands subcommand = Config.subcommandConversion.get(arguments[0]);
            if (subcommand==null) {
                source.sendMessage("No valid subcommand was found. ");
                source.sendMessage(getUsage(source));
                throw new CommandException("No valid subcommand was found. " + getUsage(source));
            } else if (source.hasPerm(subcommand.perm.get())){
                //todo: allocating here every time is probably not the best thing. Can we do something about it, without majorly changing the api?
                return subcommand.function.get().process(source, Arrays.copyOfRange(arguments, 1, arguments.length));
            } else {
                throw new CommandException(ConfigTranslate.getInstance().getNoPermission().getValue());
            }
        }else{
            source.sendMessage(getUsage(source));
            return API.getCommandResult().success();
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) throws CommandException {
        if (arguments.length == 1){
            return Arrays.stream(RebootSubcommands.values())
                    .map(Enum::name)
                    .filter(e->e.startsWith(arguments[0]))
                    .collect(Collectors.toList());
        }
        try {
            var subcommand = RebootSubcommands.valueOf(arguments[0]);
            //If you don't have permission to execute something, it shouldn't be able to be completed.
            if(!source.hasPerm(subcommand.perm.get())) return Collections.emptyList();
            //todo: allocating here every time is probably not the best thing. Can we do something about it, without majorly changing the api?
            return subcommand.function.get().getSuggestions(source,Arrays.copyOfRange(arguments, 1, arguments.length));
        }catch (IllegalArgumentException ignored){
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        String subcommands = Arrays.stream(RebootSubcommands.values())
                .filter(sc -> source.hasPerm(sc.perm.get()))
                .map(e->e.function.get().getShortDescription(source))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(s->!s.isEmpty())
                .collect(Collectors.joining("\n"));
        return Optional.of(subcommands);
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        //todo: implement
        source.sendMessage("Not Implemented. - Get Help");
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        String subcommands = Arrays.stream(RebootSubcommands.values())
                .filter(sc -> source.hasPerm(sc.perm.get()))
                .map(e->e.function.get().getUsage(source))
                .collect(Collectors.joining("\n"));
        return "Valid Usages are: \n'"+ subcommands;
    }
}
