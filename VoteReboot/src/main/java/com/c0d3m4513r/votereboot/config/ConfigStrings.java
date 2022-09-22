package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class ConfigStrings implements IConfigLoadableSaveable {
    public static ConfigStrings getInstance(){
        return Config.getInstance().getConfigStrings();
    }
    @NonNull
    private ConfigEntry<String> noVoteActive = new ConfigEntry<>(
            new ClassValue<>("No vote was running. To start a vote just type run this command with the arguments vote start",String.class),
            "votereboot.translate.noVoteActive"
    );
    @NonNull
    private ConfigEntry<String> voteAlreadyActive = new ConfigEntry<>(
            new ClassValue<>("A vote was already running.",String.class),
            "votereboot.translate.voteAlreadyActive"
    );
    @NonNull
    private ConfigEntry<String> voteStartedReply = new ConfigEntry<>(
            new ClassValue<>("Succesfully started a new Vote to restart the Server.",String.class),
            "votereboot.translate.voteStartedReply"
    );
    @NonNull
    private ConfigEntry<String> voteStartedAnnouncement = new ConfigEntry<>(
            new ClassValue<>("A Vote to restart the Server has been started. Type - vote yes or - vote no to vote.",String.class),
            "votereboot.translate.voteStartedAnnouncement"
    );
    @NonNull
    private ConfigEntry<String> voteRestartSuccess = new ConfigEntry<>(
            new ClassValue<>("It has been voted, that the server should be restarted. The server will restart in {} {}.",String.class),
            "votereboot.translate.voteRestartSuccess"
    );
    @NonNull
    private ConfigEntry<String> voteRestartFailed = new ConfigEntry<>(
            new ClassValue<>("It has been voted, that the server should not be restarted.",String.class),
            "votereboot.translate.voteRestartFailed"
    );
    @NonNull
    private ConfigEntry<String> voteYesNoError = new ConfigEntry<>(
            new ClassValue<>("The vote answer was not recognised as either yes, no or none/null. Please try again.",String.class),
            "votereboot.translate.voteYesNoError");

    @NonNull
    private ConfigEntry<String> voteSuccess = new ConfigEntry<>(
            new ClassValue<>("Successfully voted '{}'.",String.class),
            "votereboot.translate.voteSuccess");

    //----------------------------VOTE END-----------------------
    @NonNull
    private ConfigEntry<String> nowCommandResponse = new ConfigEntry<>(
            new ClassValue<>("Server will restart now!",String.class),
            "votereboot.translate.nowCommandResponse");
    @NonNull
    private RestartTypeConfig serverRestartAnnouncement = new RestartTypeConfig(
            "A Vote to restart the Server will close in {} {}.",
            "Server will restart in {} {}.",
            "Server will restart in {} {}.",
            "Server will restart in {} {}.",
            "votereboot.translate.serverRestartAnnouncement");


    //-------------------------------Generic Command Arg errors ----------------------
    @NonNull
    private ConfigEntry<String> noPermission = new ConfigEntry<>(
            new ClassValue<>("The Action failed, or you did not have enough permissions.",String.class),
            "votereboot.translate.noPermission");

    @NonNull
    private ConfigEntry<String> requiredArgs = new ConfigEntry<>(
            new ClassValue<>("Some required Arguments were missing",String.class),
            "votereboot.translate.requiredArgs");
    @NonNull
    private ConfigEntry<String> unrecognisedArgs = new ConfigEntry<>(
            new ClassValue<>("Some Arguments were unrecognised. The Action cannot continue.",String.class),
            "votereboot.translate.unrecognisedArgs");

    @NonNull
    private ConfigEntry<String> error = new ConfigEntry<>(
            new ClassValue<>("There was an internal error whilst trying to execute the Command. Please refer to the usage again.",String.class),
            "votereboot.translate.error");

    @Override
    public void loadValue() {
        noVoteActive.loadValue();
        voteAlreadyActive.loadValue();
        voteStartedReply.loadValue();
        voteStartedAnnouncement.loadValue();
        voteRestartSuccess.loadValue();
        voteYesNoError.loadValue();
        voteSuccess.loadValue();
        nowCommandResponse.loadValue();
        serverRestartAnnouncement.loadValue();
        noPermission.loadValue();
        requiredArgs.loadValue();
        unrecognisedArgs.loadValue();
        error.loadValue();
    }

    @Override
    public void saveValue() {
        noVoteActive.saveValue();
        voteAlreadyActive.saveValue();
        voteStartedReply.saveValue();
        voteStartedAnnouncement.saveValue();
        voteRestartSuccess.saveValue();
        voteYesNoError.saveValue();
        voteSuccess.saveValue();
        nowCommandResponse.saveValue();
        serverRestartAnnouncement.saveValue();
        noPermission.saveValue();
        requiredArgs.saveValue();
        unrecognisedArgs.saveValue();
        error.saveValue();
    }

    //todo: complete this
}
