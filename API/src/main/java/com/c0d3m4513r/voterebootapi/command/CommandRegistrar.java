package com.c0d3m4513r.voterebootapi.command;

import java.util.List;
import java.util.Optional;

public interface CommandRegistrar {
    Optional<CommandMapping> register(Command command, List<String> alias);
}
