package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RebootSubcommands {
    help(()->ConfigPermission.getInstance().getRebootCommand().getValue(), r->r::help),
    usage(()->ConfigPermission.getInstance().getRebootCommand().getValue(), r->r::usage),
    //this needs some more in-detail checks, depending on if we are starting a vote, or just registering one.
    vote(()->ConfigPermission.getInstance().getRebootCommand().getValue(), r->r::vote),
    start(()->ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start), r->r::start),
    now(()->ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start), r->r::now),
    //this needs some more in-detail checks, depending on what type of action we are cancelling
    cancel(()->ConfigPermission.getInstance().getRebootCommand().getValue(), r->r::cancel),
    time(()->ConfigPermission.getInstance().getRebootCommand().getValue(),r->r::time),
//    checkPerm(()->ConfigPermission.getInstance().getRebootCommand().getValue(),r->r::checkPerm),
//    timers(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.All).getPermission(Action.Read), r->r::timers),
//    getConfig(ConfigPermission.getInstance().getReload().getValue(),r->r::getConfig),
//    saveConfig(ConfigPermission.getInstance().getReload().getValue(), r->r::saveConfig),

    reloadConfig(()->ConfigPermission.getInstance().getReload().getValue(), r->r::reload);
    @NonNull
    public final Supplier<String> perm;
    @NonNull
    public final Function<Reboot,BiFunction<CommandSource, ArrayDeque<String>, CommandResult>> function;

    public static String asString(RebootSubcommands subcommand){
        switch (subcommand){
            case help: return "help";
            case usage: return "usage";
            case vote: return "vote";
            case start: return "start";
            case now: return "now";
            case cancel: return "cancel";
            case time: return "time";
            case reloadConfig: return "reloadConfig";
//            case checkPerm: return "checkPerm";
//            case getConfig:return "getConfig";
//            case saveConfig: return "saveConfig";
//            case timers: return "timers";
            default: throw new Error("Enum has more variants than expected");
        }
    }

//    public static @NonNull Optional<RebootSubcommands> valueOf(String arg){
//        return Arrays.stream(RebootSubcommands.values()).filter(e->e.toString().equals(arg)).findFirst();
//    }
}
