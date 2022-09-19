package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.Plugin;
import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.config.VoteConfigStrings;
import com.c0d3m4513r.voterebootapi.reboot.ManualAction;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class RebootStart implements CommandExecutor {
    Plugin plugin;
    @Override
    public CommandResult execute(@NonNull CommandSource src, @NonNull CommandContext args) throws CommandException {
        try{
            TimeUnit timeValue = args.<TimeUnit>getOne("d/h/m/s").get();
            long timeAmount = args.<Long>getOne("time").get();
            String reasonOP = (String) args.getOne("reason").orElse(null);
            ManualAction rta = new ManualAction(reasonOP, timeAmount,timeValue);
            if (rta.start((Permission) src)){
                return CommandResult.success();
            }else{
                throw new CommandException(Text.of(VoteConfigStrings.getNoPermission()));
            }
        }catch (NoSuchElementException nse){
            throw new CommandException(Text.of(VoteConfigStrings.getRequiredArgs().getValue()),nse,true);
        }
    }
}
