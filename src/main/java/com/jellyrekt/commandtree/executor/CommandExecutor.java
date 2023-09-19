package com.jellyrekt.commandtree.executor;

import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandExecutor<T> {
    /**
     * Execution logic for the command.
     * @param sender The entity (player or console) sending the command.
     * @param env The parameter names and argument values for this command.
     */
    void execute(CommandSender sender, Map<String, T> env);
}
