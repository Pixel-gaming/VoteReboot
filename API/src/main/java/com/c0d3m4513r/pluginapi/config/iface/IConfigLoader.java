package com.c0d3m4513r.pluginapi.config.iface;

import com.c0d3m4513r.pluginapi.Nullable;
import io.leangen.geantyref.TypeToken;

public interface IConfigLoader {
    /***
     * This needs to be Async safe.
     * This will load a Object from the config at the specified config path.
     * @param path Config Path
     * @param type Type of the value stored in the Config
     * @return Returns null or a valid Object of the specified type
     * @throws . this will throw an exception, if the value in the config could not be parsed to the requested type
     * @param <T> Type of Data stored in the config
     */
    <T> @Nullable T loadConfigKey(String path, TypeToken<T> type);
}
