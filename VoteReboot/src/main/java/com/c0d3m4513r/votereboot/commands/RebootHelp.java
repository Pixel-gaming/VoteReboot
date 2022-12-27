package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.command.Command;
import com.c0d3m4513r.pluginapi.command.CommandResult;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.config.ActionConfig;
import com.c0d3m4513r.votereboot.config.ConfigTranslateCommandHelp;
import com.c0d3m4513r.votereboot.config.ConfigPermission;
import com.c0d3m4513r.votereboot.config.RestartTypeActionConfig;
import com.c0d3m4513r.votereboot.reboot.RestartType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RebootHelp implements Command {
    public static final RebootHelp INSTANCE = new RebootHelp();
    @Override
    public @NonNull CommandResult process(CommandSource source, String[] arguments) {
        val sendHelp = Reboot.sendHelp.apply(source);
        sendHelp.apply(
                ConfigPermission.getInstance().getRebootCommand().getValue(),
                ConfigTranslateCommandHelp.getInstance().getHelpBase().getValue()
        );
        sendHelp.apply(
                ConfigPermission.getInstance().getReload().getValue(),
                ConfigTranslateCommandHelp.getInstance().getHelpReload().getValue()
        );
        sendHelp.apply(
                ConfigPermission.getInstance().getVoteRegister().getValue(),
                ConfigTranslateCommandHelp.getInstance().getHelpRegisterVote().getValue()
        );

        RestartTypeActionConfig perm = ConfigPermission.getInstance().getRestartTypeAction();
        RestartTypeActionConfig str = ConfigTranslateCommandHelp.getInstance().getHelpRestartTypeAction();
        ActionConfig strGeneral = ConfigTranslateCommandHelp.getInstance().getHelpGeneralAction();

        for (val a: Action.values()){
            final boolean include = Arrays.stream(RestartType.values())
                    .map(t->sendHelp.apply(perm.getAction(t).getPermission(a),str.getAction(t).getPermission(a)))
                    .reduce(false,Boolean::logicalOr);
            if (include && !strGeneral.getPermission(a).isEmpty()) source.sendMessage(strGeneral.getPermission(a));
        }

        return API.getCommandResult().success();
    }

    //help has no arguments
    @Override
    public List<String> getSuggestions(CommandSource source, String[] arguments) {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getShortDescription(CommandSource source) {
        return Optional.of("Gets the help for the Reboot commmand");
    }

    @Override
    public Optional<String> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/reboot help";
    }
}
