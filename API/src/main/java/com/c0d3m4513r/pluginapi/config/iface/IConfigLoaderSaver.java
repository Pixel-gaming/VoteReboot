package com.c0d3m4513r.pluginapi.config.iface;

public interface IConfigLoaderSaver extends IConfigLoader,IConfigSaver{
    /***
     * Updates the config Loader, and loads new keys
     * @return if there has been no error, and the config loader has been updated successfully
     */
    boolean updateConfigLoader();
}
