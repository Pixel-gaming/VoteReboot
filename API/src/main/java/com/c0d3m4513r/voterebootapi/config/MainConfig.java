package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.Data;

@Data
public abstract class MainConfig implements IConfigLoadableSaveable {
    public MainConfig(){
        loadValue();
    }
    public abstract void main();
}
