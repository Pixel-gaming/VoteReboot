package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.VoteConfigPermission;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.BiFunction;
import java.util.function.Function;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RebootSubcommands {
    help(VoteConfigPermission.getInstance().getRebootCommand().getValue(),r->r::help),
    usage(VoteConfigPermission.getInstance().getRebootCommand().getValue(),r->r::usage),
    //this needs some more in-detail checks, depending on if we are starting a vote, or just registering one.
    vote(VoteConfigPermission.getInstance().getRebootCommand().getValue(),r->r::vote),
    start(VoteConfigPermission.getInstance().getManualAction().getPermission(Action.Start),r->r::start),
    now(VoteConfigPermission.getInstance().getManualAction().getPermission(Action.Start),r->r::now),
    //this needs some more in-detail checks, depending on what type of action we are cancelling
    cancel(VoteConfigPermission.getInstance().getRebootCommand().getValue(),r->r::cancel),
    reloadConfig(VoteConfigPermission.getInstance().getReload().getValue(),r->r::reload),
    //todo: testing only. remove once I know, that the config saves!
    saveConfig(VoteConfigPermission.getInstance().getReload().getValue(),r->r::saveConfig);
    @NonNull
    public final String perm;
    @NonNull
    public final Function<Reboot,BiFunction<CommandSource,String[], CommandResult>> function;

    public static String asString(RebootSubcommands subcommand){
        switch (subcommand){
            case help: return "help";
            case usage: return "usage";
            case vote: return "vote";
            case start: return "start";
            case now: return "now";
            case cancel: return "cancel";
            case reloadConfig: return "reloadConfig";
            case saveConfig: return "saveConfig";
            default: throw new Error("Enum has more variants than expected");
        }
    }

//    public static @NonNull Optional<RebootSubcommands> valueOf(String arg){
//        return Arrays.stream(RebootSubcommands.values()).filter(e->e.toString().equals(arg)).findFirst();
//    }
}
