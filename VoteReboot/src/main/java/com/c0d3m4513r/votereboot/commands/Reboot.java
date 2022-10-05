package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.config.TimeEntry;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.config.VoteConfig;
import com.c0d3m4513r.votereboot.config.ConfigCommandStrings;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.ConfigStrings;
import com.c0d3m4513r.votereboot.reboot.ManualAction;
import com.c0d3m4513r.votereboot.reboot.RestartAction;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import com.c0d3m4513r.votereboot.reboot.VoteAction;
import lombok.NonNull;
import lombok.val;

import java.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

public class Reboot implements Command {
    static Function<CommandSource, BiFunction<String,String,Boolean>> sendHelp = (source)->(perm, str) -> {
        if (source.hasPerm(perm)) {
            if (!str.isEmpty()) source.sendMessage(str);
            return true;
        }else return false;
    };
    private static VoteAction voteAction = null;
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        //arg0 should just be the command alias
        if (args.length>=1) {
            RebootSubcommands subcommand = Config.subcommandConversion.get(args[0]);
            if (subcommand==null) {
                source.sendMessage("No valid subcommand was found. ");
                source.sendMessage(getUsage(source));
                throw new CommandException("No valid subcommand was found. " + getUsage(source));
            } else if (source.hasPerm(subcommand.perm.get())){
                return subcommand.function.apply(this).apply(source, Arrays.copyOfRange(args,1,args.length));
            } else {
                throw new CommandException(ConfigStrings.getInstance().getNoPermission().getValue());
            }
        }else{
            source.sendMessage(getUsage(source));
            return API.getCommandResult().success();
        }
    }

    CommandResult help(CommandSource source,String[] arguments) {
        val sendHelp = Reboot.sendHelp.apply(source);
        sendHelp.apply(
            ConfigPermission.getInstance().getRebootCommand().getValue(),
            ConfigCommandStrings.getInstance().getHelpBase().getValue()
        );
        sendHelp.apply(
            ConfigPermission.getInstance().getReload().getValue(),
            ConfigCommandStrings.getInstance().getHelpReload().getValue()
        );
        sendHelp.apply(
            ConfigPermission.getInstance().getVoteRegister().getValue(),
            ConfigCommandStrings.getInstance().getHelpRegisterVote().getValue()
        );

        RestartTypeActionConfig perm = ConfigPermission.getInstance().getRestartTypeAction();
        RestartTypeActionConfig str = ConfigCommandStrings.getInstance().getHelpRestartTypeAction();
        ActionConfig strGeneral = ConfigCommandStrings.getInstance().getHelpGeneralAction();

        for (val a: Action.values()){
            final boolean include = Arrays.stream(RestartType.values())
                    .map(t->sendHelp.apply(perm.getAction(t).getPermission(a),str.getAction(t).getPermission(a)))
                    .reduce(false,Boolean::logicalOr);
            if (include && !strGeneral.getPermission(a).isEmpty()) source.sendMessage(strGeneral.getPermission(a));
        }

        return API.getCommandResult().success();
    }
    CommandResult usage(CommandSource source,String[] arguments) {
        source.sendMessage("Not Implemented.");
        return API.getCommandResult().error();
    }
     CommandResult reload(CommandSource source,String[] arguments) {
         getLogger().info("[VoteReboot] Explicit Config load was requested. Loading configs in new async thread");
         try {
             API.runOnMain(()->{API.getConfigLoader().updateConfigLoader();Config.getInstance().loadValue();});
             //todo: Move the string to config
             source.sendMessage("Configs have been requested to reload in an async thread.");
         } catch (OutOfMemoryError oom){
             getLogger().error("[VoteReboot] Async Thread creation failed. Saving Config Synchronously Error is:",oom);
             API.getConfigLoader().updateConfigLoader();
             Config.getInstance().loadValue();
             //todo: Move the string to config
             source.sendMessage("Configs have been reloaded synchronously. There has been an error during thread creation. Please see in the console for more details.");
         }
         //todo: Move the string to config
         source.sendMessage("Configs have been loaded.");
         return API.getCommandResult().success();
    }
    CommandResult vote(CommandSource source,String[] args){
        if (args.length<1){
            source.sendMessage(ConfigStrings.getInstance().getRequiredArgs().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue()))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRegisterVote().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Vote).getPermission(Action.Start))
                || source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Start)))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Vote).getPermission(Action.Start));
            return API.getCommandResult().error();
        }



        //noinspection OptionalAssignedToNull//
        Optional<Boolean> vote = null;
        {
            for(val s: VoteConfig.getInstance().getYesList().getValue()){
                if (s.equals(args[0])) {
                    vote=Optional.of(true);
                    break;
                }
            }
            for(val s:VoteConfig.getInstance().getNoList().getValue()){
                if (s.equals(args[0])) {
                    vote=Optional.of(false);
                    break;
                }
            }
            for(val s:VoteConfig.getInstance().getNoneList().getValue()){
                if (s.equals(args[0])) {
                    vote=Optional.empty();
                    break;
                }
            }
        }

        if (args[0].equals( "start")){
            if (voteAction==null) voteAction = new VoteAction();

            if (!voteAction.isVoteInProgress()){
                if (voteAction.start(source)) {
                    getLogger().debug("Started vote.");
                    source.sendMessage(ConfigStrings.getInstance().getVoteStartedReply().getValue());
                    API.getServer().sendMessage(ConfigStrings.getInstance().getVoteStartedAnnouncement().getValue());
                    return API.getCommandResult().success();
                }else{
                    getLogger().debug("Start vote failed");
                    source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
                    return API.getCommandResult().error();
                }
            }else{
                source.sendMessage(ConfigStrings.getInstance().getVoteAlreadyActive().getValue());
                return API.getCommandResult().error();
            }
        }else //noinspection OptionalAssignedToNull//
            if (vote!=null){
            if (voteAction!=null && voteAction.isVoteInProgress()){
                if (voteAction.addVote(source,source.getIdentifier(),vote)){
                    source.sendMessage(ConfigStrings.getInstance().getVoteSuccess().getValue().replaceFirst("\\{\\}", vote.map(Object::toString).orElse("none")));
                    return API.getCommandResult().success();
                }
                //fallthrough
            }
            source.sendMessage(ConfigStrings.getInstance().getNoVoteActive().getValue());
            return API.getCommandResult().error();
        }else {
            source.sendMessage(ConfigStrings.getInstance().getUnrecognisedArgs().getValue());
            return API.getCommandResult().error();
        }
    }
    CommandResult start(CommandSource source,String[] arg){
        Supplier<CommandResult> error = () ->{
            source.sendMessage(ConfigStrings.getInstance().getError().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start)))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start));
            return API.getCommandResult().error();
        };
        if (arg.length < 1){
            source.sendMessage(ConfigStrings.getInstance().getRequiredArgs().getValue());
            return API.getCommandResult().error();
        }
        List<com.c0d3m4513r.votereboot.reboot.RestartType> types = Arrays.stream(com.c0d3m4513r.votereboot.reboot.RestartType.values()).filter(t -> com.c0d3m4513r.votereboot.reboot.RestartType.asString(t).equals(arg[0])).collect(Collectors.toList());
        //todo: use TimeEntry?
        Optional<TimeEntry> teo = TimeEntry.of(arg[0]);
        //test for wierd potential edge cases, if I mess some naming up
        if (types.size() > 1){
            getLogger().error("[VoteReboot] [start] types list contains more than one element.");
            return error.get();
        } else if (types.size() == 1 && teo.isPresent()) {
            getLogger().error("[VoteReboot] [start] types list contains one element, but also time unit.");
            return error.get();
        } else if (types.size() == 1) {
            //Delegate method
            switch (types.get(0)){
                case Vote:
                    return vote(source,Arrays.copyOfRange(arg,1,arg.length));
                case Manual:
                    //potential recursion here.
                    //If we get to max call-depth, that is the users fault, and deliberate.
                    //Even if this throws an exception, spongeforge should save us?
                    //todo: c0d3 5m3115 or code smells
                    return start(source,Arrays.copyOfRange(arg,1,arg.length));
                case Scheduled:
                    source.sendMessage("Not Implemented!");
                    return error.get();
                    //todo: not implemented
                case All:
                    //todo: better error?
                    return error.get();
                default: throw new Error("Enum has more variants than expected");
            }
        } else if (teo.isPresent()) {
            try{
                String reason = String.join(" ",Arrays.copyOfRange(arg,1,arg.length));
                TimeUnitValue tuv = teo.get().getMaxUnit();
                if(new ManualAction(reason,tuv.getValue(),tuv.getUnit()).start(source)){

                    String announcement = ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.Manual);
                    if (announcement.isEmpty()) announcement=ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(com.c0d3m4513r.votereboot.reboot.RestartType.All);
                    source.sendMessage(announcement.replaceFirst("\\{\\}",Long.toString(tuv.getValue()))
                            .replaceFirst("\\{\\}", tuv.getUnit().toString()));

                    return API.getCommandResult().success();
                }else {
                    source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
                    return API.getCommandResult().error();
                }
            }catch (NumberFormatException nfe){
                getLogger().error("[VoteReboot] Could not get parse number. Arguments are '"+ Arrays.toString(arg) +"'.",nfe);
                //todo: more detailed error
                return error.get();
            }
        }else{
            source.sendMessage(ConfigStrings.getInstance().getRequiredArgs().getValue());
            return error.get();
        }
    }
    CommandResult now(CommandSource source,@NonNull String[] arguments){
        if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start)))
        {
            API.getServer().onRestart(Optional.of(String.join(" ",arguments)));
            source.sendMessage(ConfigStrings.getInstance().getNowCommandResponse().getValue());
            return API.getCommandResult().success();
        }
        source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
        return API.getCommandResult().error();
    }
    CommandResult time(CommandSource source,String[] arguments){
        for (val ra:RestartAction.getActions()){
            if(arguments.length>=1) {
                RestartType restartType = Config.restartTypeConversion.get(arguments[0]);
                //The timer type did not match the specified type. Hiding.
                if (restartType!=null && ra.getRestartType()!=restartType) continue;
            }
            Optional<TimeUnitValue> otimer = ra.getTimer(source);
            //We do not have read permissions. Don't print this timer.
            if (!otimer.isPresent()) continue;
            TimeUnitValue timer = otimer.get();
            String start = ra.getRestartType()==RestartType.Vote?
                    "A Vote" :
                    ("A Reboot Timer of type "+ra.getRestartType());

            source.sendMessage(start +" is queued with "+timer.getValue()+" "+timer.getUnit()+ " remaining and id '"+ra.getId()+"'.");
        }
        return API.getCommandResult().success();
    }
    CommandResult cancel(CommandSource source,String[] arguments){
        if(arguments.length>=1){
            RestartType type = Config.restartTypeConversion.get(arguments[0]);
            if (type!=null){
                //unfortunate name. Optional RestartAction Latest = oral. oops
                Optional<RestartAction> oral = RestartAction.getAction(type);
                if (oral.isPresent()){
                    if (oral.get().cancelTimer(source)){
                        source.sendMessage(ConfigStrings.getInstance().getCancelActionSuccess()
                                .getValue().replaceFirst("\\{\\}",RestartType.asString(type)));
                        return API.getCommandResult().success();
                    }else {
                        source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
                        return API.getCommandResult().error();
                    }
                }else {
                    source.sendMessage(ConfigStrings.getInstance().getNoActionRestartTimer()
                            .getValue().replaceFirst("\\{\\}",RestartType.asString(type)));
                    return API.getCommandResult().error();
                }
            }else {
                HashSet<Integer> ids = Arrays.stream(arguments).parallel().map(e->{
                    try{
                        return Optional.of(Integer.parseInt(e));
                    }catch (NumberFormatException ignored){
                        return Optional.empty();
                    }
                })//Cast Type erasure back in
                        .map(e->(Optional<Integer>)e)
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
        }else {
            val sendHelp = Reboot.sendHelp.apply(source);
            RestartTypeActionConfig rtacs = ConfigCommandStrings.getInstance().getHelpRestartTypeAction();
            RestartTypeActionConfig rtacp = ConfigPermission.getInstance().getRestartTypeAction();
            final boolean include = Arrays.stream(RestartType.values())
                    .map(type -> sendHelp.apply(rtacp.getAction(type).getPermission(Action.Cancel),rtacs.getAction(type).getPermission(Action.Cancel)))
                    .reduce(false,Boolean::logicalOr);
            String generalCancel = ConfigCommandStrings.getInstance().getHelpGeneralAction().getPermission(Action.Cancel);
            if(include && !generalCancel.isEmpty()) source.sendMessage(generalCancel);
            return API.getCommandResult().error();
        }
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        source.sendMessage("Not Implemented. - Get Suggestions");
        return null;
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of(ConfigCommandStrings.getInstance().getShortDescription().getValue());
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        //todo: implement
        source.sendMessage("Not Implemented. - Get Help");
        return Optional.empty();
    }
    CommandResult checkPerm(CommandSource source,String[] args){
        for (val perm:args){
            source.sendMessage("Perm "+perm+" is set to: "+source.hasPerm(perm));
        }
        return API.getCommandResult().success();
    }

    @Override
    public String getUsage(CommandSource source) {
        List<String> subcommands = Arrays.stream(RebootSubcommands.values()).filter(sc -> source.hasPerm(sc.perm.get()))
                .map(RebootSubcommands::asString).collect(Collectors.toList());
        return "Valid Subcommands are '"+ subcommands +"'.";
    }

    private CommandResult getConfig(CommandSource commandSource, String[] strings) {
        if (!Config.DEBUG){
            throw new RuntimeException("Remove this command before releases you dummy!");
        }
        getLogger().info(Config.getInstance().toString());
        return API.getCommandResult().success();
    }
    private CommandResult timers(CommandSource source,String[] arguments) {
        if (!Config.DEBUG){
            throw new RuntimeException("Remove this command before releases you dummy!");
        }
        API.getServer().getTasks().forEach(t->{
            source.sendMessage((t.isAsynchronous()?"Async":"Sync")+" Task '"+t.getName()+"' with "+t.getDelay()+"ms delay and a Interval of "+t.getInterval()+"ms");
        });
        return API.getCommandResult().success();
    }
    private CommandResult saveConfig(CommandSource source,String[] arguments) {
        if (!Config.DEBUG){
            throw new RuntimeException("Remove this command before releases you dummy!");
        }
        getLogger().info("[VoteReboot] Explicit Config save was requested. Saving configs in new async thread");
        try {
            API.runOnMain(()->Config.getInstance().saveValue());
            //todo: Move the string to config
            source.sendMessage("[VoteReboot] Configs have been requested to be saved in an async thread.");

        } catch (OutOfMemoryError oom){
            getLogger().error("[VoteReboot] Async Thread creation failed. Saving Config Synchronously Error is:",oom);
            Config.getInstance().saveValue();
            //todo: Move the string to config
            source.sendMessage("Configs have been saved synchronously. There has been an error during thread creation. Please see in the console for more details.");
        }
        //todo: Move the string to config
        source.sendMessage("Configs have been saved.");
        return API.getCommandResult().success();
    }
}
