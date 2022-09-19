package com.c0d3m4513r.voterebootspongev7;

import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootspongev7.commands.CommandRegistrar;

public class APIImpl extends API {
    APIImpl(TaskBuilder taskBuilder, Server server,Plugin plugin) {
        //Create the actual Config.
        try {
            API.logger = plugin.logger;
            builder = new TaskBuilder(plugin);
            API.server = new Server();
            configLoader = plugin;
            config = (MainConfig) ClassLoader
                            .getSystemClassLoader()
                            .loadClass(com.c0d3m4513r.plugindef.Plugin.configClass)
                            .getConstructor()
                            .newInstance();
            commandRegistrar = new CommandRegistrar(plugin);
        }catch (Exception e){
            logger.fatal("Could not load main Config Class. Plugin Loading is now impossible. The Exception is:",e);
        }
    }
}
