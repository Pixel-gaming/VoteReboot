package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.config.iface.Loadable;
import com.c0d3m4513r.pluginapi.config.iface.Savable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class ConfigTranslate implements IConfigLoadableSaveable {
    public static ConfigTranslate getInstance(){
        return Config.getInstance().getConfigStrings();
    }
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> noVoteActive = new ConfigEntry<>(
            new ClassValue<>("No vote was running. To start a vote just type run this command with the arguments vote start",String.class),
            "votereboot.translate.noVoteActive"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteAlreadyActive = new ConfigEntry<>(
            new ClassValue<>("A vote was already running.",String.class),
            "votereboot.translate.voteAlreadyActive"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteStartedReply = new ConfigEntry<>(
            new ClassValue<>("Succesfully started a new Vote to restart the Server.",String.class),
            "votereboot.translate.voteStartedReply"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteStartedAnnouncement = new ConfigEntry<>(
            new ClassValue<>("A Vote to restart the Server has been started. Type - vote yes or - vote no to vote.",String.class),
            "votereboot.translate.voteStartedAnnouncement"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteRestartSuccess = new ConfigEntry<>(
            new ClassValue<>("It has been voted, that the server should be restarted. The server will restart in {} {}.",String.class),
            "votereboot.translate.voteRestartSuccess"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteRestartFailed = new ConfigEntry<>(
            new ClassValue<>("It has been voted, that the server should not be restarted.",String.class),
            "votereboot.translate.voteRestartFailed"
    );
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteYesNoError = new ConfigEntry<>(
            new ClassValue<>("The vote answer was not recognised as either yes, no or none/null. Please try again.",String.class),
            "votereboot.translate.voteYesNoError");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> voteSuccess = new ConfigEntry<>(
            new ClassValue<>("Successfully voted '{}'.",String.class),
            "votereboot.translate.voteSuccess");

    //----------------------------VOTE END-----------------------
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> nowCommandResponse = new ConfigEntry<>(
            new ClassValue<>("Server will restart now!",String.class),
            "votereboot.translate.nowCommandResponse");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> noActionRestartTimer = new ConfigEntry<>(
            new ClassValue<>("There was no Restart timer of type {}.",String.class),
            "votereboot.translate.noActionRestartTimer");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> cancelActionSuccess = new ConfigEntry<>(
            new ClassValue<>("The Action of type {} has been successfully cancelled.",String.class),
            "votereboot.translate.cancelActionSuccess");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> cancelActionSuccessMultiple = new ConfigEntry<>(
            new ClassValue<>("{} of {} Actions were successfully cancelled.",String.class),
            "votereboot.translate.cancelActionSuccessMultiple");
    @NonNull
    @Loadable
    @Savable
    private RestartTypeConfig serverRestartAnnouncement = new RestartTypeConfig(
            "A Vote to restart the Server will close in {} {}.",
            "Server will restart in {} {}.",
            "Server will restart in {} {}.",
            "Server will restart in {} {}.",
            "votereboot.translate.serverRestartAnnouncement");


    //-------------------------------Generic Command Arg errors ----------------------
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> noPermission = new ConfigEntry<>(
            new ClassValue<>("The Action failed, or you did not have enough permissions.",String.class),
            "votereboot.translate.noPermission");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> requiredArgs = new ConfigEntry<>(
            new ClassValue<>("Some required Arguments were missing",String.class),
            "votereboot.translate.requiredArgs");
    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> unrecognisedArgs = new ConfigEntry<>(
            new ClassValue<>("Some Arguments were unrecognised. The Action cannot continue.",String.class),
            "votereboot.translate.unrecognisedArgs");

    @NonNull
    @Loadable
    @Savable
    private ConfigEntry<String> error = new ConfigEntry<>(
            new ClassValue<>("There was an internal error whilst trying to execute the Command. Please refer to the usage again.",String.class),
            "votereboot.translate.error");

    @NonNull
    @Loadable
    @Savable
    private ShortDescription shortDescription = new ShortDescription();

}
