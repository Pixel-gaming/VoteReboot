package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.ConfigTranslate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootNow implements Command {
    public static final RebootNow INSTANCE = new RebootNow();
    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) {
        if (source.hasPerm(ConfigPermission.getInstance().getRestartTypeAction().getAction(com.c0d3m4513r.votereboot.reboot.RestartType.Manual).getPermission(Action.Start)))
        {
            API.getServer().onRestart(Optional.of(String.join(" ",arguments)));
            source.sendMessage(ConfigTranslate.getInstance().getNowCommandResponse().getValue());
            return API.getCommandResult().success();
        }
        source.sendMessage(ConfigTranslate.getInstance().getNoPermission().getValue());
        return API.getCommandResult().error();
    }

    //We cannot/will not provide suggestions for restart Reasons.
    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Restarts the server Instantly");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "reboot now <reason>";
    }
}
