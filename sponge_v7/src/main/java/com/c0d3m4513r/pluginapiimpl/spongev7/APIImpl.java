package com.c0d3m4513r.pluginapiimpl.spongev7;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandRegistrar;
import com.c0d3m4513r.pluginapiimpl.spongev7.commands.CommandResult;
import lombok.SneakyThrows;

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
