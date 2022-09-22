package com.c0d3m4513r.pluginapiimpl.spongev7;

import com.c0d3m4513r.pluginapi.Task;
import lombok.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Server extends com.c0d3m4513r.pluginapi.Server {
    Server(){
    }

    public void sendMessage(@NonNull String string) {
//        Sponge.getServer().getWorlds().forEach(world -> world.sendMessage(Text.of(string)));
        Sponge.getServer().getOnlinePlayers().forEach(p->p.sendMessage(Text.of(string)));
        Sponge.getServer().getConsole().sendMessage(Text.of(string));
    }

    public void execCommand(@NonNull String cmd) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(),cmd);
    }

    public boolean restart(Optional<String> reason) {
        if (reason.isPresent()) {
            Sponge.getServer().shutdown(Text.of(reason.get()));
        }else{
            Sponge.getServer().shutdown();
        }
        return true;
    }

    @Override
    public Set<Task> getTasks() {
        return Sponge.getScheduler().getScheduledTasks().stream().map(com.c0d3m4513r.pluginapiimpl.spongev7.Task::new).collect(Collectors.toSet());
    }
}
