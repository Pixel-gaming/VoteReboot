package com.c0d3m4513r.votereboot.config;

import com.c0d3m4513r.votereboot.Action;
import com.c0d3m4513r.votereboot.ActionPerm;
import com.c0d3m4513r.voterebootapi.config.ConfigEntry;
import com.c0d3m4513r.voterebootapi.config.iface.IConfigLoadableSaveable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.val;

@Getter
public class ActionConfig implements IConfigLoadableSaveable, ActionPerm {
    @Data
    @AllArgsConstructor
    //This class should stay as is. No funny subclassing, extending or so.
    //Also Noone has access to this besides us, and this method doesn't use any instance variables from the outer class
    private static final class SinglularActionPerm implements IConfigLoadableSaveable{
        ConfigEntry<String> action;
        Action type;
        public SinglularActionPerm(String permRoot, String configRoot, Action action){
            this.action=new ConfigEntry<String>(permRoot+"."+action.toString(action),configRoot+"."+action.toString(action));
            type=action;
        }

        @Override
        public void loadValue() {
            action.loadValue();
        }

        @Override
        public void saveValue(){
            action.saveValue();
        }
    }
    protected final SinglularActionPerm[] actionPerms = new SinglularActionPerm[(int) Action.MAX_ID];

    @Override
    public String getPermission(Action action) {
        return actionPerms[(int) action.id].getAction().getValue();
    }

    public ActionConfig(String permRoot, String configRoot){
        for(Action action:Action.values()){
            actionPerms[(int) action.id]= new SinglularActionPerm(permRoot, configRoot, action);
        }
    }

    @Override
    public void loadValue() {
        for(val sp:actionPerms){
            sp.loadValue();
        }
    }

    @Override
    public void saveValue() {
        for(val sp:actionPerms){
            sp.saveValue();
        }
    }
}
