package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.pluginapi.config.ClassValue;
import com.c0d3m4513r.pluginapi.config.ConfigEntry.ConfigEntry;
import com.c0d3m4513r.pluginapi.config.iface.IConfigLoadableSaveable;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
public class RestartTypeConfig implements IConfigLoadableSaveable {
    @Data
    @AllArgsConstructor
    //This class should stay as is. No funny subclassing, extending or so.
    //Also Noone has access to this besides us, and this method doesn't use any instance variables from the outer class
    private static final class SinglularActionPerm implements IConfigLoadableSaveable{
        ConfigEntry<String> perm;
        com.c0d3m4513r.votereboot.reboot.RestartType type;
        public SinglularActionPerm(String permRoot, String configRoot, com.c0d3m4513r.votereboot.reboot.RestartType type){
            this.perm =new ConfigEntry<>(new ClassValue<>(permRoot+"."+ com.c0d3m4513r.votereboot.reboot.RestartType.asString(type),String.class),configRoot+"."+ com.c0d3m4513r.votereboot.reboot.RestartType.asString(type));
            this.type =type;
        }

        public SinglularActionPerm(String value, String configRoot, com.c0d3m4513r.votereboot.reboot.RestartType type, boolean unused){
            this.perm =new ConfigEntry<>(new ClassValue<>(value,String.class),configRoot+"."+ com.c0d3m4513r.votereboot.reboot.RestartType.asString(type));
            this.type = type;
        }

        @Override
        public void loadValue() {
            perm.loadValue();
        }

        @Override
        public void saveValue(){
            perm.saveValue();
        }
    }
    protected final SinglularActionPerm[] RestartTypePerms = new SinglularActionPerm[(int) Action.getMaxId()];

    public String getPermission(com.c0d3m4513r.votereboot.reboot.RestartType type) {
        return RestartTypePerms[(int) type.id].getPerm().getValue();
    }

    public RestartTypeConfig(String permRoot, String configRoot){
        for(val type: com.c0d3m4513r.votereboot.reboot.RestartType.values()){
            RestartTypePerms[(int) type.id]= new SinglularActionPerm(permRoot, configRoot, type);
        }
    }
    public RestartTypeConfig(String vote, String manual, String scheduled, String all, String configRoot){
        RestartTypePerms[(int) com.c0d3m4513r.votereboot.reboot.RestartType.Vote.id]= new SinglularActionPerm(vote, configRoot, com.c0d3m4513r.votereboot.reboot.RestartType.Vote, false);
        RestartTypePerms[(int) com.c0d3m4513r.votereboot.reboot.RestartType.Manual.id]= new SinglularActionPerm(manual, configRoot, com.c0d3m4513r.votereboot.reboot.RestartType.Manual, false);
        RestartTypePerms[(int) com.c0d3m4513r.votereboot.reboot.RestartType.Scheduled.id]= new SinglularActionPerm(scheduled, configRoot, com.c0d3m4513r.votereboot.reboot.RestartType.Scheduled, false);
        RestartTypePerms[(int) com.c0d3m4513r.votereboot.reboot.RestartType.All.id]= new SinglularActionPerm(all, configRoot, com.c0d3m4513r.votereboot.reboot.RestartType.All, false);
    }

    @Override
    public void loadValue() {
        for(val sp: RestartTypePerms){
            sp.loadValue();
        }
    }

    @Override
    public void saveValue() {
        for(val sp: RestartTypePerms){
            sp.saveValue();
        }
    }
}
