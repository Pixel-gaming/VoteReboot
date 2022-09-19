package com.c0d3m4513r.voterebootapi;

import com.c0d3m4513r.voterebootapi.command.CommandRegistrar;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoaderSaver;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

public abstract class API {
    @Getter
    protected static Server server;
    @Getter
    protected static TaskBuilder builder;
    @Getter
    protected static Logger logger;
    @Getter
    protected static IConfigLoaderSaver configLoader;
    @Getter
    protected static MainConfig config;
    @Getter
    protected static CommandRegistrar commandRegistrar;

    //todo: add logger
}
