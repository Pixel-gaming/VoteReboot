package com.c0d3m4513r.votereboot;

import com.c0d3m4513r.voterebootapi.config.VoteConfig;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class Server implements com.c0d3m4513r.voterebootapi.Server {

    @Override
    public boolean sendMessage(@NonNull String string) {
        Sponge.getServer().getWorlds().forEach(world -> world.sendMessage(Text.of(string)));
        return true;
    }

    @Override
    public void execCommand(@NonNull String cmd) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(),cmd);
    }

    @Override
    public boolean restart() {
        if (VoteConfig.getKickEnabled().getValue())
            Sponge.getServer()
                    .getOnlinePlayers()
                    .forEach(
                            VoteConfig.getUseCustomMessage().getValue() ?
                                    player -> player.kick(Text.of(VoteConfig.getCustomMessage().getValue()))
                                    : Player::kick);
        onBeforeRestart();
        if(VoteConfig.getUseCustomMessage().getValue()) Sponge.getServer().shutdown(Text.of(VoteConfig.getCustomMessage().getValue()));
        else Sponge.getServer().shutdown();
        return true;
    }

    @Override
    public void voteRestart() {
        com.c0d3m4513r.voterebootapi.Server.super.voteRestart();
    }

    @Override
    public void voteFailed() {
        com.c0d3m4513r.voterebootapi.Server.super.voteFailed();
    }
}
