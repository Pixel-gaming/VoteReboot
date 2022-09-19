package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.voterebootapi.events.EventRegistrar;
import com.c0d3m4513r.voterebootapi.events.EventType;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    private VoteConfig voteConfig = null;

    @Override
    public void loadValue() {
        voteConfig.loadValue();
    }

    @Override
    public void saveValue() {
        voteConfig.saveValue();
    }

    @Override
    public void main() {
        new EventRegistrar(this::registerCommands, EventType.commandRegister,0);
    }

    private void registerCommands(){
        API.getCommandRegistrar().register(new Reboot(),voteConfig.getAliasList().getValue());
    }
}
