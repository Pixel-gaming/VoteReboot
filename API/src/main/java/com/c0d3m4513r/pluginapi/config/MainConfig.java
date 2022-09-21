package com.c0d3m4513r.pluginapi.config;

import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.Data;

import java.util.Optional;

@Data
public abstract class MainConfig implements IConfigLoadableSaveable {
    public MainConfig(){}
    public abstract void main();
    public abstract Optional<String> getDefaultConfigContents();
}
