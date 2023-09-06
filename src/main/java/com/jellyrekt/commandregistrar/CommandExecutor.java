package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.Map;

public interface CommandExecutor {
    /**
     * Execution logic for the command.
     * @param sender The entity (player or console) sending the command.
     * @param env The parameter names and argument values for this command.
     */
    public void execute(CommandSender sender, Map<String, String> env);
}
