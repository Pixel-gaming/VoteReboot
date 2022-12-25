package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandException;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.*;
import com.c0d3m4513r.votereboot.reboot.VoteAction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootVote implements Command {
    @org.jetbrains.annotations.NotNull
    public static final RebootVote INSTANCE = new RebootVote();
    @Nullable
    private VoteAction voteAction = null;

    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) throws CommandException {
        if (arguments.length < 1){
            source.sendMessage(ConfigTranslate.getInstance().getRequiredArgs().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getVoteRegister().getValue()))
                source.sendMessage(ConfigTranslateCommandHelp.getInstance().getHelpRegisterVote().getValue());
            if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Vote).getPermission(Action.Start))
                    || source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Start)))
                source.sendMessage(ConfigTranslateCommandHelp.getInstance().getHelpRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Vote).getPermission(Action.Start));
            return API.getCommandResult().error();
        }
        var arg0 = arguments[0];
        if (arg0 == null) throw new CommandException("Expected a non-Null argument, but got null argument");

        //noinspection OptionalAssignedToNull//
        Optional<Boolean> vote = null;
        {
            if (VoteConfig.getInstance().getYesList().getValue().contains(arg0)) vote=Optional.of(true);
            else if (VoteConfig.getInstance().getNoList().getValue().contains(arg0)) vote=Optional.of(false);
            else if (VoteConfig.getInstance().getNoneList().getValue().contains(arg0)) vote=Optional.empty();
        }

        if (arg0.equals("start")){
            return startVote(source);
        }else
        //noinspection OptionalAssignedToNull//
        if(vote != null)
            return addVote(source, vote);
        else {
            source.sendMessage(ConfigTranslate.getInstance().getUnrecognisedArgs().getValue());
            return API.getCommandResult().error();
        }
    }

    @NonNull
    private CommandResult startVote(CommandSource source) {
        if (voteAction==null) voteAction = new VoteAction();
        if( voteAction.isVoteInProgress()){
            source.sendMessage(ConfigTranslate.getInstance().getVoteAlreadyActive().getValue());
            return API.getCommandResult().error();
        }

        if (voteAction.start(source)) {
            getLogger().debug("Started vote.");
            source.sendMessage(ConfigTranslate.getInstance().getVoteStartedReply().getValue());
            API.getServer().sendMessage(ConfigTranslate.getInstance().getVoteStartedAnnouncement().getValue());
            return API.getCommandResult().success();
        }else{
            source.sendMessage(ConfigTranslate.getInstance().getNoPermission().getValue());
            return API.getCommandResult().error();
        }

    }

    @NonNull
    private CommandResult addVote(@NotNull CommandSource source, @NotNull Optional<Boolean> vote){
        if(voteAction == null || !voteAction.isVoteInProgress()){
            source.sendMessage(ConfigTranslate.getInstance().getNoVoteActive().getValue());
            return API.getCommandResult().error();
        }
        if (voteAction.addVote(source,source.getIdentifier(),vote)){
            source.sendMessage(ConfigTranslate.getInstance().getVoteSuccess().getValue().replaceFirst("\\{}", vote.map(Object::toString).orElse("none")));
            return API.getCommandResult().success();
        }
        source.sendMessage(ConfigTranslate.getInstance().getNoPermission().toString());
        return API.getCommandResult().error();
    }

    @NonNull
    private static List<String> getPossibleArguments(){
        var suggestionList = new LinkedList<String>();
        suggestionList.add("start");
        suggestionList.addAll(VoteConfig.getInstance().getYesList().getValue());
        suggestionList.addAll(VoteConfig.getInstance().getNoList().getValue());
        suggestionList.addAll(VoteConfig.getInstance().getNoneList().getValue());
        return suggestionList;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        if (arguments.length < 1 || arguments[0] == null) return Collections.emptyList();



        return getPossibleArguments().stream().filter(e -> e.startsWith(arguments[0])).collect(Collectors.toList());
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Here you can vote yes, no or abstain to restart the server");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "["+ String.join("/", getPossibleArguments()) +"]";
    }
}
