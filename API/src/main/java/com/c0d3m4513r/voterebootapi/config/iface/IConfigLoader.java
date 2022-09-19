package com.c0d3m4513r.voterebootapi.config.iface;

import com.c0d3m4513r.voterebootapi.Nullable;

import java.util.Optional;

public interface IConfigLoader {
    <T> @Nullable T loadConfigKey(String path, Class<T> type);
}
