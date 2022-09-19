package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.config.VoteConfigStrings;
import com.c0d3m4513r.voterebootapi.reboot.Action;
import com.c0d3m4513r.voterebootapi.reboot.ManualAction;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RebootNow implements CommandExecutor, Consumer<ManualAction> {

    @Override
    public CommandResult execute(@NonNull CommandSource src, @NonNull CommandContext args) throws CommandException {
        ManualAction timedAction = new ManualAction(args.<String>getOne("reason").orElse(null));
        if(timedAction.start((Permission) src)){
            return CommandResult.success();
        }else{
            Action.actions.remove(timedAction);
            throw new CommandException(Text.of(VoteConfigStrings.getNoPermission()));
        }
    }

    @Override
    public void accept(ManualAction timedAction) {
        Sponge.getServer().shutdown(Text.of(timedAction.reason));
    }
}
