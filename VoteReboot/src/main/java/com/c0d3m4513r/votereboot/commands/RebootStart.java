package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.pluginapi.config.TimeEntry;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.ConfigTranslateCommandHelp;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.ConfigTranslate;
import com.c0d3m4513r.votereboot.reboot.ManualAction;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.var;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootStart implements Command {
    public static final RebootStart INSTANCE = new RebootStart();
    Function<CommandSource ,CommandResult> error = source ->{
        source.sendMessage(ConfigTranslate.getInstance().getError().getValue());
        if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start)))
            source.sendMessage(ConfigTranslateCommandHelp.getInstance().getHelpRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start));
        return API.getCommandResult().error();
    };
    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) throws CommandException {
        if (arguments.length < 1 || arguments[0] == null){
            source.sendMessage(ConfigTranslate.getInstance().getRequiredArgs().getValue());
            return API.getCommandResult().error();
        }
        var tryDelegateResult = tryDelegate(source, arguments);
        if (tryDelegateResult.isPresent()) return tryDelegateResult.get();

        int usedArgs;
        Optional<TimeEntry> teo = Optional.empty();
        if (arguments.length >= 2) teo = TimeEntry.of(arguments[0], arguments[1]);
        if (!teo.isPresent()){
            usedArgs = 1;
            teo = TimeEntry.of(arguments[0]);
        }
        else {
            usedArgs = 2;
        }
        //at this point, something is wrong with the input.
        if (!teo.isPresent()){
            source.sendMessage(ConfigTranslate.getInstance().getUnrecognisedArgs().toString());
            return API.getCommandResult().error();
        }

        try{
            String reason = String.join(" ", Arrays.asList(arguments).subList(usedArgs - 1, arguments.length));
            if (reason.equals("")) reason = null;
            TimeUnitValue tuv = teo.get().getMaxUnit();
            if(!new ManualAction(reason,tuv).start(source)) {
                source.sendMessage(ConfigTranslate.getInstance().getNoPermission().getValue());
                return API.getCommandResult().error();
            }
            String announcement = ConfigTranslate.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.Manual);
            if (announcement.isEmpty()) announcement= ConfigTranslate.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.All);
            source.sendMessage(announcement.replaceFirst("\\{}",Long.toString(tuv.getValue()))
                    .replaceFirst("\\{}", tuv.getUnit().toString()));

            return API.getCommandResult().success();
        }catch (NumberFormatException nfe){
            getLogger().error("[VoteReboot] Could not get parse number.",nfe);
            //todo: more detailed error
            return error.apply(source);
        }
    }
    private Optional<CommandResult> tryDelegate(CommandSource source, String[] arguments) throws CommandException {
        List<com.c0d3m4513r.votereboot.reboot.RestartType> types = Arrays.stream(RestartType.values()).filter(t -> t.name().equals(arguments[0])).collect(Collectors.toList());
        //test for wierd potential edge cases, if I mess some naming up
        if (types.size() > 1){
            getLogger().error("[VoteReboot] [start] types list contains more than one element.");
            return Optional.of(error.apply(source));
        }
        if (types.size() <= 0) return Optional.empty();
        //Delegate method
        switch (types.get(0)){
            case Vote:
                return Optional.of(RebootSubcommands.vote.function.get().process(source, arguments));
            case Manual:
                //todo: allocating here every time is probably not the best thing. Can we do something about it, without majorly changing the api?
                return Optional.of(RebootSubcommands.start.function.get().process(source, Arrays.copyOfRange(arguments, 1, arguments.length)));
            case Scheduled:
                //todo: config customisable
                source.sendMessage("Not Implemented!");
                //todo: not implemented
                return Optional.of(error.apply(source));
            case All:
                //todo: config customisable
                source.sendMessage("Not Supported!");
                //todo: better error?
                return Optional.of(error.apply(source));
            default: throw new Error("Enum has more variants than expected");
        }
    }

    //there are no reasonable suggestions we could make for a Time period or a Restart Reason.
    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Launches Other Actions, or manually sets a time after which the server will restart.");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "[TimeEntry] <Reason:String>";
    }
}
