package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.Data;

import java.util.Optional;

@Data
public abstract class MainConfig implements IConfigLoadableSaveable {
    public MainConfig(){}
    public abstract void main();
    public abstract Optional<String> getDefaultConfigContents();
}
