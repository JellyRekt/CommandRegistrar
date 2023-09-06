package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class CommandTreeNode {
    private Set<CommandTreeNode> children = new HashSet<>();
    private Map<String, String> childAliases = new HashMap<>();
    private String description;
    private String usage;
    private CommandExecutor commandExecutor;

    CommandTreeNode(String description, String usage, CommandExecutor commandExecutor) {
        this.description = description;
        this.usage = usage;
        this.commandExecutor = commandExecutor;
    }

    void execute(CommandSender sender, Map<String, String> env) {
        // TODO
    }
}
