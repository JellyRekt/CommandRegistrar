package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class CommandTreeNode {
    /**
     * Nodes containing the subcommand of this node's command
     */
    private Set<CommandTreeNode> children = new HashSet<>();
    /**
     * Maps a child command's alias to its actual key.
     * The key itself is registered as an alias.
     */
    private Map<String, String> childAliases = new HashMap<>();
    /**
     * Description for the command contained in this node
     */
    private String description;
    /**
     * Usage message for the command contained in this node
     */
    private String usage;
    /**
     * Executor to handle the command contained in this node
     */
    private CommandExecutor commandExecutor;

    /**
     * Construct a new node.
     *
     * @param description
     * @param usage
     * @param commandExecutor
     */
    CommandTreeNode(String description, String usage, CommandExecutor commandExecutor) {
        this.description = description;
        this.usage = usage;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Execute the command contained in this node.
     *
     * @param sender
     * @param env
     */
    void execute(CommandSender sender, Map<String, String> env) {
        // TODO
    }

    /**
     * Register a subcommand under this command.
     *
     * @param key         Key (first token) of the subcommand
     * @param aliases     Strings which are accepted as
     * @param description Description for the subcommand
     * @param usage       Usage message for the subcommand
     * @param executor    CommandExecutor to handle the command
     */
    void register(String key, Set<String> aliases, String description, String usage, CommandExecutor executor) {
        aliases.add(key);
        for (String alias : aliases) {
            childAliases.put(alias, key);
        }
        children.add(new CommandTreeNode(description, usage, executor));
    }
}
