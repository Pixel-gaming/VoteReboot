package com.c0d3m4513r.voterebootspongev7;

import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootspongev7.commands.CommandRegistrar;
import com.c0d3m4513r.voterebootspongev7.commands.CommandResult;
import lombok.SneakyThrows;

import java.net.URLClassLoader;

public class APIImpl extends API {
    @SneakyThrows
    APIImpl(Plugin plugin) {
        super();
        API.logger = plugin.logger;
        builder = new TaskBuilder(plugin);
        API.server = new Server();
        configLoader = plugin;

        commandRegistrar = new CommandRegistrar(plugin);
        commandResult = new CommandResult();
        config.main();
    }
}
