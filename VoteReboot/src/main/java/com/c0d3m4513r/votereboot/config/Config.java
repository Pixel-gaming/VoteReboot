package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.voterebootapi.events.EventRegistrar;
import com.c0d3m4513r.voterebootapi.events.EventType;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;

import static com.c0d3m4513r.voterebootapi.API.getLogger;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    @NonNull
    private VoteConfig voteConfig = new VoteConfig();

    @Override
    public void loadValue() {
        getLogger().info("[VoteReboot] Load Config");
        voteConfig.loadValue();
    }

    @Override
    public void saveValue() {
        getLogger().info("[VoteReboot] Save Config");
        voteConfig.saveValue();
    }

    @Override
    public void main() {
        getLogger().info("[VoteReboot] Registering Command Register Hook");
        new EventRegistrar(this::registerCommands, EventType.commandRegister,0);
    }

    private void registerCommands(){
        getLogger().info("[VoteReboot] Register Commands");
        API.getCommandRegistrar().register(new Reboot(), Arrays.asList(voteConfig.getAliasList().getValue().getValue()));
    }
}
