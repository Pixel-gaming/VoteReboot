package com.c0d3m4513r.votereboot.commands;

import com.c0d3m4513r.votereboot.Plugin;
import com.c0d3m4513r.voterebootapi.reboot.*;
import com.c0d3m4513r.voterebootapi.config.VoteConfig;
import com.c0d3m4513r.voterebootapi.config.VoteConfigPermission;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Reboot {
    @NonNull
    Plugin plugin;
    @NonNull
    static Optional<VoteAction> voteAction = Optional.empty();

    public CommandSpec loadCommands(){
        CommandSpec help = CommandSpec.builder()
                .description(Text.of("List of commands usable to the player"))
                .executor(new RebootHelp())
                .build();
        final Map<String, RestartType> coicesRestartType = new HashMap<String,RestartType>(){
            {
                put("vote",RestartType.Vote);
                put("v",RestartType.Vote);
                put("manual",RestartType.ManualRestart);
                put("m",RestartType.ManualRestart);
                put("scheduled",RestartType.Scheduled);
                put("s",RestartType.Scheduled);
            }
        };
        final Map<String, CancelSelector> coicesCancelSelector = new HashMap<String,CancelSelector>(){
            {
                put("latest", CancelSelector.Latest);
                put("l", CancelSelector.Latest);
                put("all", CancelSelector.All);
                put("a", CancelSelector.All);
                put("earliest", CancelSelector.Earliest);
                put("e", CancelSelector.Earliest);

            }
        };
        CommandSpec cancel = CommandSpec.builder()
                .description(Text.of("Cancels an active restart"))
                .executor(new RebootCancel())
                .permission(VoteConfigPermission.getCancel().getValue())
                .arguments(
                        GenericArguments.choices(Text.of("vote/manual/scheduled"),coicesRestartType,true,false),
                        GenericArguments.choices(Text.of("latest/earliest/all"),coicesCancelSelector,true,false)
                ).build();
        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reloads the Config-File"))
                .permission(VoteConfigPermission.getReload().getValue())
                .build();

        final Map<String, TimeUnit> choicesTimeUnit = new HashMap<String,TimeUnit>() {
            {
                put("d",TimeUnit.DAYS);
                put("h", TimeUnit.HOURS);
                put("m", TimeUnit.MINUTES);
                put("s", TimeUnit.SECONDS);
            }
        };

        CommandSpec start = CommandSpec.builder()
                .description(Text.of("Restarts the server in a specified amount of Time"))
                //Todo: add permission
                .arguments(GenericArguments.choices(Text.of("d/h/m/s"),choicesTimeUnit),
                        GenericArguments.longNum(Text.of("time")),
                        GenericArguments.remainingRawJoinedStrings(Text.of("reason")))
                .executor(new RebootStart(plugin))
                .build();

        CommandSpec time = CommandSpec.builder()
                .description(Text.of("Displays the amount of time left, before a restart"))
                //Todo: add permission
                .executor(new RebootTime())
                .build();
        CommandSpec now = CommandSpec.builder()
                .description(Text.of("Restarts the server instantly"))
                //Todo: add permission
                .arguments(GenericArguments.remainingRawJoinedStrings(Text.of("reason")))
                .executor(new RebootNow())
                .build();

        Map<String, Optional<Boolean>> choicesBoolean = new HashMap<>();
        VoteConfig.getYesList().getValue().forEach(yes->choicesBoolean.put(yes,Optional.of(true)));
        VoteConfig.getNoList().getValue().forEach(no->choicesBoolean.put(no,Optional.of(false)));
        VoteConfig.getNoneList().getValue().forEach(none->choicesBoolean.put(none,Optional.empty()));

        CommandSpec vote = CommandSpec.builder()
                //Todo: add permission
                .description(Text.of("Vote to restart the server"))
                .arguments(
                        GenericArguments.optional(
                                GenericArguments.choices(Text.of("true/false/null/none"),choicesBoolean,true,false),
                                GenericArguments.remainingRawJoinedStrings(Text.of("uuid"))
                        ))
                .executor(new RebootStart(plugin))
                .build();

        return CommandSpec.builder()
                .description(Text.of("Base reboot Command"))
                .permission(VoteConfigPermission.getRebootCommand().getValue())
                .child(help,"help")
                .child(cancel,"cancel")
                .child(reload,"reload")
                .child(start,"start")
                .child(time,"time")
                .child(now, "now")
                .build();
    }
}
