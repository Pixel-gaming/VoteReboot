package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ConfigEntry<T extends Object> implements IConfigLoadableSaveable {
    @NonNull
    public T value;
    public String configPath;

    private ConfigEntry(){
        loadValue();
    }

    @SuppressWarnings("unchecked")
    public void saveValue(){
        API.getConfigLoader().saveConfigKey(value,(Class<T>) value.getClass(), configPath);
    }

    @SuppressWarnings("unchecked")
    public void loadValue(){
        T val = API.getConfigLoader().loadConfigKey(configPath,(Class<T>) value.getClass());
        if(val!=null){
            value=val;
        }else{
            saveValue();
        }
    }

    //todo: tbh, I don't know how many rules I'm breaking here, but I'm sure spongeforge, spigot, bukkit and forge break more
    //      Either way, this is not pretty. Is there a better solution?
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (T)value;
    }

}