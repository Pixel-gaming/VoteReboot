package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.convert.Convert;
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
import com.c0d3m4513r.votereboot.reboot.RestartType;
import com.c0d3m4513r.votereboot.reboot.VoteAction;
import lombok.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import static com.c0d3m4513r.pluginapi.API.getLogger;

public class Reboot implements Command {
    private static VoteAction voteAction = null;
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        String[] args = arguments.split(" ");
        //arg0 should just be the command alias
        if (args.length>=1) {
            List<RebootSubcommands> subcommand = Arrays.stream(RebootSubcommands.values()).filter(s -> RebootSubcommands.asString(s).equals(args[0])).collect(Collectors.toList());
            if (subcommand.isEmpty()) {
                source.sendMessage("No valid subcommand was found. ");
                source.sendMessage(getUsage(source));
                throw new CommandException("No valid subcommand was found. " + getUsage(source));
            } else if (subcommand.size() > 1) {
                //todo: add to config
                getLogger().warn("[VoteReboot] Provided String matched multiple Subcommands? '"+arguments+"'.");
                source.sendMessage("Provided String matched multiple Subcommands?");
                throw new CommandException("Provided String matched multiple Subcommands?");
            } else {
                return subcommand.get(0).function.apply(this).apply(source, Arrays.copyOfRange(args,1,args.length));
            }
        }else{
            source.sendMessage(getUsage(source));
            return API.getCommandResult().success();
        }
    }

    CommandResult timers(CommandSource source,String[] arguments) {
        API.getServer().getTasks().forEach(t->{
            source.sendMessage((t.isAsynchronous()?"Async":"Sync")+" Task '"+t.getName()+"' with "+t.getDelay()+"ms delay and a Interval of "+t.getInterval()+"ms");
        });
        return API.getCommandResult().success();
    }

    CommandResult help(CommandSource source,String[] arguments) {
        BiConsumer<String,String> sendHelp = (perm,str) -> {
            if (source.hasPerm(perm)) if (!str.isEmpty()) source.sendMessage(str);
        };
        sendHelp.accept(
            ConfigPermission.getInstance().getRebootCommand().getValue(),
            ConfigCommandStrings.getInstance().getHelpBase().getValue()
        );
        sendHelp.accept(
            ConfigPermission.getInstance().getReload().getValue(),
            ConfigCommandStrings.getInstance().getHelpReload().getValue()
        );
        sendHelp.accept(
            ConfigPermission.getInstance().getVoteRegister().getValue(),
            ConfigCommandStrings.getInstance().getHelpRegisterVote().getValue()
        );

        RestartTypeActionConfig perm = ConfigPermission.getInstance().getRestartTypeAction();
        RestartTypeActionConfig str = ConfigCommandStrings.getInstance().getHelpRestartTypeAction();
        ActionConfig strGeneral = ConfigCommandStrings.getInstance().getHelpGeneralAction();

        for (val a:Action.values()){
            boolean include = false;
            for (val t: RestartType.values()){
                sendHelp.accept(perm.getAction(t).getPermission(a),str.getAction(t).getPermission(a));
                if (source.hasPerm(perm.getAction(t).getPermission(a))){
                    include=true;
                }
            }
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
             API.getBuilder().reset().async(true).name("votereboot-A-loadConfig").executer(()->{API.getConfigLoader().updateConfigLoader();Config.getInstance().loadValue();}).build();
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
    CommandResult saveConfig(CommandSource source,String[] arguments) {
        getLogger().info("[VoteReboot] Explicit Config save was requested. Saving configs in new async thread");
        try {
            API.getBuilder().reset().async(true).executer(()->Config.getInstance().saveValue()).build();
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
    CommandResult vote(CommandSource source,String[] args){
        if (args.length<1){
            source.sendMessage(ConfigStrings.getInstance().getRequiredArgs().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue()))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRegisterVote().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(RestartType.Vote).getPermission(Action.Start))
                || source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(RestartType.All).getPermission(Action.Start)))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRestartTypeAction().getAction(RestartType.Vote).getPermission(Action.Start));
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
            if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(RestartType.ManualRestart).getPermission(Action.Start)))
                source.sendMessage(ConfigCommandStrings.getInstance().getHelpRestartTypeAction().getAction(RestartType.ManualRestart).getPermission(Action.Start));
            return API.getCommandResult().error();
        };
        if (arg.length < 1){
            source.sendMessage(ConfigStrings.getInstance().getRequiredArgs().getValue());
            return API.getCommandResult().error();
        }
        List<RestartType> types = Arrays.stream(RestartType.values()).filter(t -> RestartType.asString(t).equals(arg[0])).collect(Collectors.toList());
        //todo: use TimeEntry?
        Optional<TimeUnit> timeUnit = Convert.asTimeUnit(arg[0]);
        //test for wierd potential edge cases, if I mess some naming up
        if (types.size() > 1){
            getLogger().error("[VoteReboot] [start] types list contains more than one element.");
            return error.get();
        } else if (types.size() == 1 && timeUnit.isPresent()) {
            getLogger().error("[VoteReboot] [start] types list contains one element, but also time unit.");
            return error.get();
        } else if (types.size() == 1) {
            //Delegate method
            switch (types.get(0)){
                case Vote:
                    return vote(source,Arrays.copyOfRange(arg,1,arg.length));
                case ManualRestart:
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
        } else if (timeUnit.isPresent()) {
            try{
                long timeAmount = Long.parseLong(arg[1]);
                String reason = String.join(" ",Arrays.copyOfRange(arg,2,arg.length));
                if(new ManualAction(reason,timeAmount,timeUnit.get()).start(source)){

                    String announcement = ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(RestartType.ManualRestart);
                    if (announcement.isEmpty()) announcement=ConfigStrings.getInstance().getServerRestartAnnouncement().getPermission(RestartType.All);
                    source.sendMessage(announcement.replaceFirst("\\{\\}",Long.toString(timeAmount))
                            .replaceFirst("\\{\\}", timeUnit.get().toString()));

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
        if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(RestartType.ManualRestart).getPermission(Action.Start)))
        {
            API.getServer().onRestart(Optional.of(String.join(" ",arguments)));
            source.sendMessage(ConfigStrings.getInstance().getNowCommandResponse().getValue());
            return API.getCommandResult().success();
        }
        source.sendMessage(ConfigStrings.getInstance().getNoPermission().getValue());
        return API.getCommandResult().error();
    }
    CommandResult cancel(CommandSource source,String[] arguments){
        source.sendMessage("Not Implemented.");
        return API.getCommandResult().error();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        source.sendMessage("Not Implemented. - Get Suggestions");
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPerm(ConfigPermission.getInstance().getRebootCommand().getValue());
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

    @Override
    public String getUsage(CommandSource source) {
        List<String> subcommands = Arrays.stream(RebootSubcommands.values()).filter(sc -> source.hasPerm(sc.perm))
                .map(RebootSubcommands::asString).collect(Collectors.toList());
        return "Valid Subcommands are '"+ subcommands +"'.";
    }

    public CommandResult getConfig(CommandSource commandSource, String[] strings) {
        getLogger().info(Config.getInstance().toString());
        return API.getCommandResult().success();
    }
}
