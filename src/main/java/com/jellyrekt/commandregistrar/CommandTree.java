package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Set;

public class CommandTree extends CommandTreeNode {

    /**
     * Register a command.
     *
     * @param command     Full command string
     * @param aliases     Strings which are accepted as aliases for this command
     * @param description Description for the command
     * @param usage       Usage message for the command
     * @param executor    CommandExecutor to handle the command
     */
    public void register(String command, Set<String> aliases, String description, String usage, CommandExecutor executor) {
        super.register(command, aliases, description, usage, executor);
    }

    /**
     * Execute a command.
     * @param sender
     * @param command
     */
    public void execute(CommandSender sender, String command) {
        execute(sender, command, new HashMap<>());
    }
}
