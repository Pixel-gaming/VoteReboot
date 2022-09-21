package com.c0d3m4513r.pluginapiimpl.spongev7;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Nullable;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoaderSaver;
import com.c0d3m4513r.pluginapi.events.EventRegistrar;
import com.c0d3m4513r.pluginapi.events.EventType;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import org.slf4j.Logger;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@org.spongepowered.api.plugin.Plugin(
        id = com.c0d3m4513r.plugindef.Plugin.id,
        name = com.c0d3m4513r.plugindef.Plugin.name,
        description = com.c0d3m4513r.plugindef.Plugin.description,
        version = com.c0d3m4513r.plugindef.Plugin.version
)
public class Plugin implements IConfigLoaderSaver {
    @NonNull
    @Inject(optional = true)
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @NonNull
    @Inject(optional = true)
    @DefaultConfig(sharedRoot = false)
    private Path configFile;

    @NonNull
    private YAMLConfigurationLoader configurationLoader;
    private ConfigurationNode root;
    @Getter(AccessLevel.PUBLIC)
    @NonNull
    final Logger logger;
    private final APIImpl api;

    @Inject
    public Plugin(@NonNull PluginContainer container,@NonNull final Logger logger) throws IOException {
        logger.info("[sponge-v7] Construct start");
        //Init config stuff
        if (configDir==null){
            logger.warn("[sponge-v7] Manually getting config-dir from sponge, because the Injector did not inject the config dir");
            configDir = Sponge.getConfigManager().getPluginConfig(container).getDirectory();
        }
        configDir.toFile().mkdirs();
        if (configFile==null){
            logger.warn("[sponge-v7] Manually constructing config-file, because the Injector did not inject the config dir");
            configFile = configDir.resolve("config.yml");
        }
        logger.info("[sponge-v7] The Plugin Config directory is '"+configDir.toString()+"'.");
        logger.info("[sponge-v7] The Plugin Config File is '"+configFile.toString()+"'.");
        //Init config loader
        this.logger=logger;
        //init api, register events
        api=new APIImpl(this);
        {
            File configFileFile = new File(configFile.toUri());
            if (configFileFile.createNewFile()) {
                logger.info("[spring-v7] The Config file is missing. Creating new config-file");
                FileWriter fw = new FileWriter(configFileFile);
                BufferedWriter writer = new BufferedWriter(fw);
                logger.info("[spring-v7] Created Writers");
                Optional<String> os = API.getConfig().getDefaultConfigContents();
                if (os.isPresent()){
                    logger.info("[spring-v7] Got default String");
                    writer.write(os.get());
                    writer.flush();
                    logger.info("[spring-v7] Wrote default Config");
                }else {
                    logger.info("[spring-v7] Got no String from the getDefaultConfigContents method.");
                }
                writer.close();
                fw.close();
            }
        }

        configurationLoader = YAMLConfigurationLoader.builder().setIndent(2).setPath(configFile).build();
        root=configurationLoader.load();
        logger.info("[sponge-v7] Construct end");
    }

    @Listener
    public void PreInit(GamePreInitializationEvent event){
        logger.info("[sponge-v7] PreInit start");
        APIImpl.getConfig().loadValue();
        EventRegistrar.submitEvent(EventType.preinit);
        logger.info("[sponge-v7] PreInit end");
    }
    @Listener
    public void Init(GameInitializationEvent event){
        logger.info("[sponge-v7] Init start");
        EventRegistrar.submitEvent(EventType.commandRegister);
        EventRegistrar.submitEvent(EventType.init);
        logger.info("[sponge-v7] Init end");
    }

    @Listener
    public void load(GameLoadCompleteEvent event){
        logger.info("[sponge-v7] LoadComplete start");
        EventRegistrar.submitEvent(EventType.load_complete);
        logger.info("[sponge-v7] LoadComplete end");
    }

    @Override
    public <T> @Nullable T loadConfigKey(String path, Class<T> type){
        try{
            return root.getNode((Object[]) path.split("\\.")).getValue(TypeToken.of(type));
        }catch (ObjectMappingException e){
            logger.error("Failed to serialise Path:'" + path + "' because of", e);
            return null;
        }
    }

    @Override
    public <T> boolean saveConfigKey(@Nullable T value, @NonNull Class<T> typeToken, @NonNull String path) {
        try {
            root.getNode((Object[]) path.split("\\.")).getValue(TypeToken.of(typeToken),value);
        } catch (ObjectMappingException e) {
            logger.error("Could not save config at Path:'"+path+"', because of",e);
            return false;
        }
        return true;
    }
}
