package com.c0d3m4513r.voterebootspongev7;

import com.c0d3m4513r.voterebootapi.Nullable;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoaderSaver;
import com.c0d3m4513r.voterebootapi.events.EventRegistrar;
import com.c0d3m4513r.voterebootapi.events.EventType;
import com.google.inject.Inject;
import io.leangen.geantyref.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;


@org.spongepowered.api.plugin.Plugin(
        id = com.c0d3m4513r.plugindef.Plugin.id,
        name = com.c0d3m4513r.plugindef.Plugin.name,
        description = com.c0d3m4513r.plugindef.Plugin.description,
        version = com.c0d3m4513r.plugindef.Plugin.version
)
public class Plugin implements IConfigLoaderSaver {
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private Path configFile = Paths.get(configDir + "/config.yml");

    private YamlConfigurationLoader configurationLoader = YamlConfigurationLoader.builder().indent(2).path(configFile).build();
    private ConfigurationNode root;
    @Getter(AccessLevel.PUBLIC)
    final Logger logger;
    final PluginContainer container;
    private final APIImpl api;

    @Inject
    Plugin(final PluginContainer container, final Logger logger) throws ConfigurateException {
        logger.info("[sponge-v7] Construct start");
        this.container=container;
        this.logger=logger;
        root=configurationLoader.load();
        api=new APIImpl(this);
        logger.info("[sponge-v7] Construct end");
    }

    @Listener
    public void PreInit(GamePreInitializationEvent event){
        logger.info("[sponge-v7] PreInit start");
        APIImpl.getConfig().loadValue();
        logger.info("[sponge-v7] PreInit end");
    }
    @Listener
    public void Init(GameInitializationEvent event){
        logger.info("[sponge-v7] Init start");
        EventRegistrar.submitEvent(EventType.commandRegister);
        logger.info("[sponge-v7] Init end");
    }

    @Override
    public @Nullable <T> T loadConfigKey(String path, TypeToken<T> type){
        try{
            return root.node((Object[]) path.split("\\.")).get(type);
        }catch (SerializationException e){
            logger.error("Failed to serialise Path:'" + path + "' because of", e);
            return null;
        }
    }

    @Override
    public <T> boolean saveConfigKey(@Nullable T value, @NonNull TypeToken<T> type, @NonNull String path) {
        try {
            root.node((Object[]) path.split("\\.")).set(type,value);
        } catch (SerializationException e) {
            logger.error("Could not save config at Path:'"+path+"', because of",e);
            return false;
        }
        return true;
    }
}
