package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Supplier;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RebootSubcommands {
    help(()->ConfigPermission.getInstance().getRebootCommand().getValue(), ()->RebootHelp.INSTANCE),
    //this needs some more in-detail checks, depending on if we are starting a vote, or just registering one.
    vote(()->ConfigPermission.getInstance().getRebootCommand().getValue(), ()->RebootVote.INSTANCE),
    start(()->ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start), ()->RebootStart.INSTANCE),
    now(()->ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start), () -> RebootNow.INSTANCE),
    //this needs some more in-detail checks, depending on what type of action we are cancelling
    cancel(()->ConfigPermission.getInstance().getRebootCommand().getValue(), () -> RebootCancel.INSTANCE),
    time(()->ConfigPermission.getInstance().getRebootCommand().getValue(),()->RebootTime.INSTANCE),

    reloadConfig(()->ConfigPermission.getInstance().getReload().getValue(), ()->RebootReload.INSTANCE);
    @NonNull
    public final Supplier<String> perm;
    @NonNull
    public final Supplier<Command> function;
}
