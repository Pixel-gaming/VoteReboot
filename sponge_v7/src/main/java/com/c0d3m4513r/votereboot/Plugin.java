package com.c0d3m4513r.votereboot;

import com.c0d3m4513r.votereboot.commands.Reboot;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoader;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.c0d3m4513r.voterebootapi.config.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@org.spongepowered.api.plugin.Plugin(id = "votereboot",name = "VoteReboot", description = "A Plugin to allow to VoteRestart and AutoRestart a server", authors = {"C0D3 M4513R"})
public class Plugin implements IConfigLoader {
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
    Plugin(final PluginContainer container, final Logger logger){
        this.container=container;
        this.logger=logger;
        new TaskBuilder(this);
    }

    @Listener
    public void Init(GameInitializationEvent event){
        loadCommands();
    }
    private void loadCommands() {
        Reboot reboot=new Reboot(this);
        Sponge.getCommandManager().register(this,reboot.loadCommands(),"reboot", "restart");
    }

    @Override
    //todo: tbh, I don't know how many rules I'm breaking here, but I'm sure spongeforge, spigot, bukkit and forge break more
    //      Either way, this is not pretty. Is there a better solution?
    public void loadConfig(){
        //todo: handle all of the RSMCConfig's
        //Hacks, Hacks and more Hacks
        try{
           root=configurationLoader.load();
           //Grab all Configs, that need population.
           List<Field> fields = new ArrayList<>(Arrays.asList(VoteConfigPermission.class.getFields()));
           fields.addAll(Arrays.asList(VoteConfig.class.getFields()));
           fields.addAll(Arrays.asList(Config.class.getFields()));
           //and For every one, that is a ConfigEntry
           for(Field field:fields){
               if (field.getType().equals(ConfigEntry.class)){
                   //get the ConfigEntry, and config value
                   ConfigEntry<?> entry = (ConfigEntry<?>)field.get(null);
                   ConfigurationNode node = root.node((Object[]) entry.configPath.split("\\."));
                   Object val = node.get((Type) entry.value.getClass());

                   //And update, if the config indeed contains something.
                   if (val!=null) ConfigEntry.class.getField("value").set(entry,val);
                   //If the config contains nothing, we update the config instead
                   else node.set(entry.value);

               }
           }
        }catch (Exception e){
            logger.fatal("Failed to serialise the config", e);
        }
    }
    public void saveConfig() throws ConfigurateException {
        configurationLoader.save(root);
    }
}
