package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.Plugin;
import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.config.VoteConfigStrings;
import com.c0d3m4513r.voterebootapi.reboot.VoteAction;
import com.c0d3m4513r.voterebootapi.config.VoteConfig;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Identifiable;

import java.util.Optional;

public class RebootVote implements CommandExecutor {
    @Override
    public CommandResult execute(@NonNull CommandSource src, @NonNull CommandContext args) throws CommandException {
        Optional<Optional<Boolean>> vote = args.getOne("true/false");
        if (!vote.isPresent() && Reboot.voteAction.isPresent()){
            throw new CommandException(Text.of(VoteConfigStrings.getNoVoteActive().getValue()));
        } else if (vote.isPresent() && Reboot.voteAction.isPresent()) {
            Optional<Boolean> votec = vote.get();
            VoteAction voteAction = Reboot.voteAction.get();
            if (src instanceof Identifiable){
                if (voteAction.addVote((Permission) src,((Identifiable)src).getUniqueId().toString(), votec)){
                    return CommandResult.success();
                }else throw new CommandException(Text.of(VoteConfigStrings.getNoPermission().getValue()));
            }
            else if (src instanceof ConsoleSource){
                if (voteAction.addVote((Permission)Sponge.getServer().getConsole(),args.<String>getOne("uuid").orElse("CONSOLE"), votec)){
                    return CommandResult.success();
                }else throw new CommandException(Text.of(VoteConfigStrings.getNoPermission().getValue()));
            }
            else throw new CommandException(Text.of("Only Players and Console may vote"));
        }else {
            VoteAction voteAction=new VoteAction();
            if (voteAction.start((Permission) src)){
                Reboot.voteAction=Optional.of(voteAction);
                return CommandResult.success();
            }else{
                throw new CommandException(Text.of(VoteConfigStrings.getNoPermission().getValue()));
            }
        }
    }
}
