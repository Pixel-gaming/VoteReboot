package com.c0d3m4513r.voterebootapi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ConfigEntry<T extends Object> {
    @NonNull
    public T value;
    public String configPath;

    //todo: tbh, I don't know how many rules I'm breaking here, but I'm sure spongeforge, spigot, bukkit and forge break more
    //      Either way, this is not pretty. Is there a better solution?
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (T)value;
    }
}