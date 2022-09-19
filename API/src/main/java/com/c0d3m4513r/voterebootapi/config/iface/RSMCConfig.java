package com.c0d3m4513r.voterebootapi.config.iface;

import com.c0d3m4513r.voterebootapi.RSMCPerm;
import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import lombok.Getter;

@Getter
public class RSMCConfig extends RSMCPerm {
    ConfigEntry<String> read;
    ConfigEntry<String> start;
    ConfigEntry<String> modify;
    ConfigEntry<String> cancel;

    public RSMCConfig(String permRoot, String configRoot){
        read = new ConfigEntry<>(permRoot+".read",configRoot+".read");
        start = new ConfigEntry<>(permRoot+".start",configRoot+".start");
        modify = new ConfigEntry<>(permRoot+".modify",configRoot+".modify");
        cancel = new ConfigEntry<>(permRoot+".cancel",configRoot+".cancel");
        readPermission = read.getValue();
        startPermission = start.getValue();
        modifyPermission = modify.getValue();
        cancelPermission = cancel.getValue();
    }
}
