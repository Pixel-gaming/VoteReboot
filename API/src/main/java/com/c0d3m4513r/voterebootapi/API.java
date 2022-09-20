package com.c0d3m4513r.voterebootapi;

import com.c0d3m4513r.voterebootapi.command.CommandRegistrar;
import com.c0d3m4513r.voterebootapi.command.CommandResult;
import com.c0d3m4513r.voterebootapi.config.MainConfig;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoaderSaver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.net.URLClassLoader;

public abstract class API {
    @Getter
    protected static Server server;
    @Getter
    protected static TaskBuilder builder;
    @Getter(AccessLevel.PUBLIC)
    protected static Logger logger;
    @Getter
    protected static IConfigLoaderSaver configLoader;
    @Getter
    protected static MainConfig config;
    @Getter
    protected static CommandRegistrar commandRegistrar;
    @Getter
    protected static CommandResult commandResult;

    @SneakyThrows
    protected API(){
        try(URLClassLoader loader = URLClassLoader.newInstance(
                new java.net.URL[]{API.class.getProtectionDomain().getCodeSource().getLocation()},
                getClass().getClassLoader())) {
                config = (MainConfig) loader
                .loadClass(com.c0d3m4513r.plugindef.Plugin.configClass)
                .getConstructor()
                .newInstance();
        }
    }
}
