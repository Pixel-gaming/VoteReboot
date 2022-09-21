package com.c0d3m4513r.pluginapi.events;

import lombok.ToString;

@ToString
public enum EventType {
    onReboot,
    preinit,
    init,
    load_complete,
    commandRegister,
}
