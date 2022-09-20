package com.c0d3m4513r.voterebootapi.config.iface;

import com.c0d3m4513r.voterebootapi.Nullable;
import io.leangen.geantyref.TypeToken;

public interface IConfigLoader {
    <T> @Nullable T loadConfigKey(String path, TypeToken<T> type);
}
