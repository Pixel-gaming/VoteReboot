package com.c0d3m4513r.voterebootapi.config.iface;

import com.c0d3m4513r.voterebootapi.Nullable;
import io.leangen.geantyref.TypeToken;
import lombok.NonNull;

public interface IConfigSaver {
    /***
     * This needs to be Async safe.
     * This will save a Object from the config at the specified config path.
     * @param path Config Path
     * @param typeToken Type of the value stored in the Config
     * @param value Value to be stored in the config
     * @return Returns null or a valid Object of the specified type
     * @throws . this will throw an exception, if the value could not be serialised
     * @param <T> Type of Data stored in the config
     */
    <T> boolean saveConfigKey(@Nullable T value, @NonNull TypeToken<T> typeToken, @NonNull String path);
}
