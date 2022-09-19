package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.voterebootapi.Permission;
import com.c0d3m4513r.voterebootapi.config.VoteConfigStrings;
import com.c0d3m4513r.voterebootapi.reboot.Action;
import com.c0d3m4513r.voterebootapi.reboot.CancelSelector;
import com.c0d3m4513r.voterebootapi.reboot.RestartType;
import lombok.NonNull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RebootCancel implements CommandExecutor {
    @Override
    public CommandResult execute(@NonNull CommandSource src, @NonNull CommandContext args) throws CommandException {
        try {
            RestartType type = args.<RestartType>getOne("vote/manual/scheduled").get();
            CancelSelector cancelSelector = args.<CancelSelector>getOne("latest/earliest/all").get();
            Stream<Action> stream = Action.actions.stream().filter(e->e.restartType == type);
            long count = stream.count();
            if (cancelSelector==CancelSelector.All){
                stream.forEach(action -> {
                    action.cancelTimer((Permission) src);
                    Action.actions.remove(action);
                });
                return CommandResult.successCount((int)count);
            } else if (cancelSelector == CancelSelector.Earliest || cancelSelector == CancelSelector.Latest) {
                List<Action> list = stream.sorted((e1, e2)-> (int) (e1.getTimerPermless()-e2.getTimerPermless())).collect(Collectors.toList());
                //Get correct action
                Action action;
                if (list.isEmpty()){
                    return CommandResult.empty();
                } else if (cancelSelector == CancelSelector.Earliest) {
                    action = list.get(0);
                }else {
                    action = list.get(list.size()-1);
                }
                //Try to cancel
                if (action.cancelTimer((Permission) src)){
                    Action.actions.remove(action);
                    return CommandResult.success();
                }
                throw new CommandException(Text.of(VoteConfigStrings.getNoPermission()));
            }else{
                throw new CommandException(Text.of("The CancelSelector Enum and Command Argument had more variants, than were expected."));
            }
        }catch (NoSuchElementException e){
            throw new CommandException(Text.of(VoteConfigStrings.getRequiredArgs()),e,true);
        }
    }
}
