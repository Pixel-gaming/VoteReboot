package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.votereboot.reboot.RestartAction;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import lombok.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootCancel  implements Command {

    public static final RebootCancel INSTANCE = new RebootCancel();

    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) {
        if (arguments.length < 1){
            val sendHelp = Reboot.sendHelp.apply(source);
            RestartTypeActionConfig restartTypeActionStrings = ConfigCommandStrings.getInstance().getHelpRestartTypeAction();
            RestartTypeActionConfig restartTypeACtionPermissions = ConfigPermission.getInstance().getRestartTypeAction();
            final boolean include = Arrays.stream(RestartType.values())
                    .map(type -> sendHelp.apply(restartTypeACtionPermissions.getAction(type).getPermission(Action.Cancel),restartTypeActionStrings.getAction(type).getPermission(Action.Cancel)))
                    .reduce(false,Boolean::logicalOr);
            String generalCancel = ConfigCommandStrings.getInstance().getHelpGeneralAction().getPermission(Action.Cancel);
            if(include && !generalCancel.isEmpty()) source.sendMessage(generalCancel);
            return API.getCommandResult().error();
        }

        RestartType type = Config.restartTypeConversion.get(arguments[0]);
        if (type!=null){
            //unfortunate name. Optional RestartAction Latest = oral. oops
            Optional<RestartAction> oral = RestartAction.getAction(type);

            if (!oral.isPresent()){
                source.sendMessage(ConfigStrings.getInstance().getNoActionRestartTimer()
                        .getValue().replaceFirst("\\{\\}",RestartType.asString(type)));
                return API.getCommandResult().error();
            }

            if (!oral.get().cancelTimer(source)){
                source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
                return API.getCommandResult().error();
            }

            source.sendMessage(ConfigStrings.getInstance().getCancelActionSuccess()
                    .getValue().replaceFirst("\\{\\}",RestartType.asString(type)));
            return API.getCommandResult().success();
        }else {
            HashSet<Integer> ids = Arrays.stream(arguments).parallel().map((Function<String, Optional<Integer>>)  e->{
                        try{
                            return Optional.of(Integer.parseInt(e));
                        }catch (NumberFormatException ignored){
                            return Optional.empty();
                        }
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(HashSet::new));
            int n = RestartAction.cancel(source,ids);
            if (n>0){
                source.sendMessage(ConfigStrings.getInstance().getCancelActionSuccessMultiple().getValue()
                        .replaceFirst("\\{\\}",Integer.toString(n))
                        .replaceFirst("\\{\\}",Integer.toString(ids.size()))
                );
                return API.getCommandResult().success();
            }else{
                source.sendMessage(ConfigStrings.getInstance().getUnrecognisedArgs().getValue());
                return API.getCommandResult().error();
            }
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        var actions = Arrays.stream(Action.values()).map(Enum::name);
        if (arguments.length > 1)
            actions = actions.filter(e->e.startsWith(arguments[0]));
        return actions.collect(Collectors.toList());
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Cancels a currently active Reboot timer.");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "[ids:uint/Action]";
    }
}