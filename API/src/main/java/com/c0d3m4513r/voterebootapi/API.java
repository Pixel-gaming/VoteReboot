package com.c0d3m4513r.voterebootapi;

import lombok.Getter;

public abstract class API {
    @Getter
    protected static Server server;
    @Getter
    protected static TaskBuilder builder;
    //todo: add logger
}
