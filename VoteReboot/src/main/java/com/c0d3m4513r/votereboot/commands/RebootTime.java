package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.votereboot.config.Config;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.reboot.RestartAction;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import lombok.NonNull;
import lombok.val;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RebootTime implements Command {
    public final static RebootTime INSTANCE = new RebootTime();

    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) {
        Consumer<RestartAction> print_restart_action = (ra) ->  {
            if(arguments.length>=1) {
                RestartType restartType = Config.restartTypeConversion.get(arguments[0]);
                //The timer type did not match the specified type. Hiding.
                if (restartType!=null && ra.getRestartType()!=restartType) return;
            }
            Optional<TimeUnitValue> otimer = ra.getTimer(source);
            //We do not have read permissions. Don't print this timer.
            if (!otimer.isPresent()) return;
            TimeUnitValue timer = otimer.get();
            String start = ra.getRestartType()==RestartType.Vote?
                    "A Vote" :
                    ("A Reboot Timer of type "+ra.getRestartType());

            source.sendMessage(start +" is queued with "+timer.getValue()+" "+timer.getUnit()+ " remaining and id '"+ra.getId()+"'.");
        };
        //either the Restart Action is not overridden, or it is and the user has permission to see the overridden timers.
        if (!RestartAction.isOverridden() || source.hasPerm(ConfigPermission.getInstance().getViewOverridenTimers().getValue())) {
            for (val ra : RestartAction.getActions()) {
                print_restart_action.accept(ra);
            }
        }
        //If we do have an overridden timer we display it though (if the user has read permission for that timer type of course).
        if(RestartAction.isOverridden()){
            source.sendMessage("All timers above are overridden by a manual reboot. They will not trigger a reboot. The only timer that will count, is the timer below (until it is cancelled)");
            print_restart_action.accept(RestartAction.getOverride());
        }

        return API.getCommandResult().success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        return null;
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
