package com.c0d3m4513r.votereboot.config;


import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.config.MainConfig;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import lombok.*;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.c0d3m4513r.pluginapi.API.getLogger;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class Config extends MainConfig implements IConfigLoadableSaveable {
    @NonNull
    private VoteConfig voteConfig = new VoteConfig();

    public static Config getInstance(){
        return ((Config)API.getConfig());
    }

    @Override
    public void loadValue() {
        getLogger().info("[VoteReboot] Load Config");
        voteConfig.loadValue();
        getLogger().info("[VoteReboot] Load Config Done");
    }

    @Override
    public void saveValue() {
        getLogger().info("[VoteReboot] Save Config");
        voteConfig.saveValue();
        getLogger().debug(voteConfig.toString());
        getLogger().info("[VoteReboot] Save Config Done");
    }

    @Override
    public void main() {
        getLogger().info("[VoteReboot] Registering Command Register Hook");
        new EventRegistrar(this::registerCommands, EventType.commandRegister,0);
    }

    @Override
    public Optional<String> getDefaultConfigContents() {
        try(
//                URLClassLoader loader = URLClassLoader.newInstance(new URL[]{Config.class.getProtectionDomain().getCodeSource().getLocation()});
                InputStream is = Objects.requireNonNull(Config.class.getResourceAsStream("/config.yml"));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            return Optional.of(br.lines().collect(Collectors.joining("\n")));
        }catch (NullPointerException | IOException e){
            //We have no default config?
            getLogger().error("Exception occurred whilst trying to read default config",e);
            return Optional.empty();
        }
    }

    private void registerCommands(){
        getLogger().info("[VoteReboot] Register Commands");
        API.getCommandRegistrar().register(new Reboot(), Arrays.asList(voteConfig.getAliasList().getValue()));
    }
}
