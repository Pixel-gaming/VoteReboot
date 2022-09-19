package com.c0d3m4513r.voterebootspongev7;

import com.c0d3m4513r.voterebootapi.Nullable;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoaderSaver;
import com.c0d3m4513r.voterebootapi.events.EventRegistrar;
import com.c0d3m4513r.voterebootapi.events.EventType;
import com.google.inject.Inject;
import lombok.NonNull;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;


@org.spongepowered.api.plugin.Plugin(id = "votereboot",name = "VoteReboot", description = "A Plugin to allow to VoteRestart and AutoRestart a server", authors = {"C0D3 M4513R"})
public class Plugin implements IConfigLoaderSaver {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public File defaultConfFile;
    @Inject
    private ConfigurationLoader<CommentedConfigurationNode> configurationLoader =
            YamlConfigurationLoader.builder().indent(2).file(defaultConfFile).build();
    private ConfigurationNode root;
    final Logger logger;
    final PluginContainer container;

    @Inject
    Plugin(final PluginContainer container, final Logger logger) throws ConfigurateException {
        this.container=container;
        this.logger=logger;
        root=configurationLoader.load();
        new TaskBuilder(this);
    }

    @Listener
    public void Init(GameInitializationEvent event){
        loadCommands();
    }
    private void loadCommands() {
        EventRegistrar.submitEvent(EventType.commandRegister);
    }

    @Override
    public <T> @Nullable T loadConfigKey(String path,Class<T> type){
        try{
            return root.node((Object[]) path.split("\\.")).get(type);
        }catch (SerializationException e){
            logger.error("Failed to serialise Path:'" + path + "' because of", e);
            return null;
        }
    }

    @Override
    public <T> boolean saveConfigKey(@Nullable T value, @NonNull Class<T> tClass, @NonNull String path) {
        try {
            root.node((Object[]) path.split("\\.")).set(tClass,value);
        } catch (SerializationException e) {
            logger.error("Could not save config at Path:'"+path+"', because of",e);
            return false;
        }
        return true;
    }
}
