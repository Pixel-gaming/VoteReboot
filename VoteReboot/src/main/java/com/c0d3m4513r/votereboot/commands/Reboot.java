package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.votereboot.config.VoteConfigCommandStrings;
import com.c0d3m4513r.votereboot.config.VoteConfigPermission;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

public class Reboot implements Command {

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        getLogger().info("{}", (Object) args);//todo: remove this log, once the command distributer here works
        //arg0 should just be the command alias
        if (args.length>=1) {
            List<RebootSubcommands> subcommand = Arrays.stream(RebootSubcommands.values()).filter(s -> RebootSubcommands.asString(s).equals(args[0])).collect(Collectors.toList());
            if (subcommand.isEmpty()) {
                source.sendMessage("No valid subcommand was found. ");
                source.sendMessage(getUsage(source));
                throw new CommandException("No valid subcommand was found. " + getUsage(source));
            } else if (subcommand.size() > 1) {
                //todo: add to config
                getLogger().warn("Provided String matched multiple Subcommands? '"+arguments+"'.");
                source.sendMessage("Provided String matched multiple Subcommands?");
                throw new CommandException("Provided String matched multiple Subcommands?");
            } else {
                return subcommand.get(0).function.apply(this).apply(source, args);
            }
        }else{
            source.sendMessage(getUsage(source));
            return API.getCommandResult().success();
        }
    }

    CommandResult help(CommandSource source,String[] arguments) {
        return null;
    }
    CommandResult usage(CommandSource source,String[] arguments) {
        return null;
    }
     CommandResult reload(CommandSource source,String[] arguments) {
         getLogger().info("Explicit Config load was requested. Loading configs in new async thread");
         try {
             API.getBuilder().async(true).executer(()->Config.getInstance().loadValue()).build();
             //todo: Move the string to config
             source.sendMessage("Configs have been requested to reload in an async thread.");
         } catch (OutOfMemoryError oom){
             getLogger().error("Async Thread creation failed. Saving Config Synchronously Error is:",oom);
             Config.getInstance().saveValue();
             //todo: Move the string to config
             source.sendMessage("Configs have been reloaded synchronously. There has been an error during thread creation. Please see in the console for more details.");
         }
         //todo: Move the string to config
         source.sendMessage("Configs have been loaded.");
         return API.getCommandResult().success();
    }
    CommandResult saveConfig(CommandSource source,String[] arguments) {
        getLogger().info("Explicit Config save was requested. Saving configs in new async thread");
        try {
            API.getBuilder().reset().async(true).executer(()->Config.getInstance().saveValue()).build();
            //todo: Move the string to config
            source.sendMessage("Configs have been requested to be saved in an async thread.");

        } catch (OutOfMemoryError oom){
            getLogger().error("Async Thread creation failed. Saving Config Synchronously Error is:",oom);
            Config.getInstance().saveValue();
            //todo: Move the string to config
            source.sendMessage("Configs have been saved synchronously. There has been an error during thread creation. Please see in the console for more details.");
        }
        //todo: Move the string to config
        source.sendMessage("Configs have been saved.");
        return API.getCommandResult().success();
    }
    CommandResult vote(CommandSource source,String[] arguments){
        return null;
    }
    CommandResult start(CommandSource source,String[] arguments){
        return null;
    }
    CommandResult now(CommandSource source,String[] arguments){
        return null;
    }
    CommandResult cancel(CommandSource source,String[] arguments){
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPerm(VoteConfigPermission.getInstance().getRebootCommand().getValue());
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of(VoteConfigCommandStrings.getInstance().getShortDescription().getValue());
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        //todo: implement
        return Optional.of(getUsage(source));
    }

    @Override
    public String getUsage(CommandSource source) {
        List<String> subcommands = Arrays.stream(RebootSubcommands.values()).filter(sc -> source.hasPerm(sc.perm))
                .map(RebootSubcommands::asString).collect(Collectors.toList());
        return "Valid Subcommands are '"+ subcommands +"'.";
    }
}
