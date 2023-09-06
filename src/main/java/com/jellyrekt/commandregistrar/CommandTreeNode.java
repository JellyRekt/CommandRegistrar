package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class CommandTreeNode {
    /**
     * Nodes containing the subcommand of this node's command
     */
    private Map<String, CommandTreeNode> children = new HashMap<>();
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
     * Execute the command contained in this node.
     *
     * @param sender
     * @param env
     */
    void execute(CommandSender sender, Map<String, String> env) {
        commandExecutor.execute(sender, env);
    }

    /**
     * Register a subcommand under this command.
     *
     * @param subcommand  Key (first token) of the subcommand
     * @param aliases     Strings which are accepted as
     * @param description Description for the subcommand
     * @param usage       Usage message for the subcommand
     * @param executor    CommandExecutor to handle the command
     */
    void register(String subcommand, Set<String> aliases, String description, String usage, CommandExecutor executor) {
        // Consume the first token to use as a key
        String[] split = subcommand.split(" ", 2);
        String key = split[0];
        subcommand = split[1];
        // Consume parameters
        // Parameters right now are assumed to be preceded by a ':'.
        // Thus, all I am doing is trimming these off the beginning of the string.
        // I am not tracking what param keys are expected.
        subcommand = subcommand.replaceFirst("(:.* *)*", "");
        // Get or insert the next node in the command tree
        CommandTreeNode node = get(key);
        if (node == null) {
            node = children.put(key, new CommandTreeNode());
        }
        // If this is the end of the command being registered, set aliases, description, and usage.
        // This means commands can be registered out of order, like first /foo bar and then /foo
        // It also means certain subcommands may not have an executor, if they are not a complete command.
        if (!subcommand.trim().isEmpty()) {
            node.description = description;
            node.usage = usage;
            node.commandExecutor = executor;
            // Add a reference to the child node
            aliases.add(key);
            for (String alias : aliases) {
                childAliases.put(alias, key);
            }
        }
    }

    /**
     * Get the child node with the given key or alias
     *
     * @param alias Subcommand key or alias
     * @return Node containing the subcommand
     */
    CommandTreeNode get(String alias) {
        return children.get(childAliases.get(alias));
    }
}
