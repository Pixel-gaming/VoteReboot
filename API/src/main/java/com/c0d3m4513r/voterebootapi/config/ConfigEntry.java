package com.c0d3m4513r.voterebootapi.config;

import com.c0d3m4513r.voterebootapi.API;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import io.leangen.geantyref.TypeToken;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
/***
 * @type v This is the regular type, e.g. List with String type Parameter
 * @type t This is the due to type erasure
 */
public class ConfigEntry<V> implements IConfigLoadableSaveable {
    @NonNull
    public ClassValue<V> value;
    @NonNull
    public String configPath;

    public void saveValue(){
        API.getConfigLoader().saveConfigKey(value.getValue(),TypeToken.get(value.getClazz()), configPath);
    }

    public void loadValue(){
        V val = API.getConfigLoader().loadConfigKey(configPath,TypeToken.get(value.getClazz()));
        if(val!=null){
            value.setValue(val);
        }else{
            saveValue();
        }
    }

}