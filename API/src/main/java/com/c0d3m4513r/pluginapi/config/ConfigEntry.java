package com.c0d3m4513r.pluginapi.config;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@RequiredArgsConstructor
@Setter(AccessLevel.NONE)
/***
 * @type v This is the regular type, e.g. List with String type Parameter
 * @type t This is the due to type erasure
 */
public class ConfigEntry<V> implements IConfigLoadableSaveable {
    @NonNull
    @Getter(AccessLevel.NONE)
    public ClassValue<V> value;
    @NonNull
    public String configPath;

    public void saveValue(){
        API.getConfigLoader().saveConfigKey(value.getValue(),value.getClazz(), configPath);
    }

    public void loadValue(){
        V val = API.getConfigLoader().loadConfigKey(configPath,value.getClazz());
        if(val!=null){
            if (!value.getValue().equals(val)) API.getLogger().info("For config string '{}' replacing '{}' with new Value '{}'",configPath,value.getValue(),val);
            value.setValue(val);
        }else{
            saveValue();
        }
    }
    public V getValue(){
        return value.getValue();
    }

}