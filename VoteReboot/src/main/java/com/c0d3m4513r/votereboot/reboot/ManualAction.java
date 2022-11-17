package com.c0d3m4513r.votereboot.reboot;

import com.c0d3m4513r.pluginapi.API;
import com.c0d3m4513r.pluginapi.Nullable;
import com.c0d3m4513r.pluginapi.command.CommandSource;
import com.c0d3m4513r.pluginapi.config.TimeUnitValue;
import com.c0d3m4513r.votereboot.Action;
import lombok.*;

import java.util.concurrent.TimeUnit;

import static com.c0d3m4513r.pluginapi.API.getLogger;

public class ManualAction extends RestartAction {

    public ManualAction(@Nullable String reason, @NonNull TimeUnitValue tuv){
        super(RestartType.Manual,tuv);
        this.reason = reason;
    }

    public boolean start(CommandSource perm){
        if (hasPerm(perm, restartType, Action.Start)){
            if (!isOverridden()) perm.sendMessage("Overriding all other timers, until the manual timer gets cancels (or triggers a reboot)");
            else{
                perm.sendMessage("Overriding another Manual reboot.");
                override.cancelTimer(true);
            }
            override = this;
            intStart(true);
            return true;
        } return false;
    }

}
