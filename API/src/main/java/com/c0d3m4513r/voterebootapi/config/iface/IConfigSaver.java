package com.c0d3m4513r.voterebootapi.config.iface;

import com.c0d3m4513r.voterebootapi.Nullable;
import lombok.NonNull;

public interface IConfigSaver {
    <T> boolean saveConfigKey(@Nullable T value, @NonNull Class<T> tClass, @NonNull String path);
}
