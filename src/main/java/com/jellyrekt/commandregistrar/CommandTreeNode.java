package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.*;

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
     * List of parameters that should receive arguments when the command is executed
     */
    private List<String> paramList = new ArrayList<>();

    /**
     * Get the child node with the given key or alias
     *
     * @param alias Subcommand key or alias
     * @return Node containing the subcommand
     */
    CommandTreeNode get(String alias) {
        return children.get(childAliases.get(alias));
    }

    /**
     * Register a subcommand under this command.
     *
     * @param subcommand  Key (first token) of the subcommand
     * @param aliases     Strings which are accepted as aliases for this command
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
        Scanner scanner = new Scanner(subcommand);
        String next = "";
        while (scanner.hasNext()) {
            next = scanner.next();
            // If not a param
            if (next.charAt(0) != ':') {
                break;
            }
            // Trim off the : and add to the param list.
            paramList.add(next.substring(1));
        }
        // Turn the remainder of the string into the subcommand
        StringBuilder builder = new StringBuilder(next);
        while (scanner.hasNext()) {
            builder.append(scanner.next());
        }
        scanner.close();
        subcommand = builder.toString();
        // Get or insert the next node in the command tree
        CommandTreeNode node = get(key);
        if (node == null) {
            node = children.put(key, new CommandTreeNode());
        }
        // If this is not the end of the subcommand
        if (!subcommand.trim().isEmpty()) {
            node.register(subcommand, aliases, description, usage, executor);
            return;
        }
        // If this is the end of the command being registered, set aliases, description, and usage.
        // This means commands can be registered out of order, like first /foo bar and then /foo
        // It also means certain subcommands may not have an executor, if they are not a complete command.
        node.description = description;
        node.usage = usage;
        node.commandExecutor = executor;
        // Add a reference to the child node
        aliases.add(key);
        for (String alias : aliases) {
            childAliases.put(alias, key);
        }
    }

    /**
     * Execute the given command
     * @param sender
     * @param command
     * @param env
     */
    protected void execute(CommandSender sender, String command, Map<String, String> env) {
        // Base case: we've arrived at the final node
        if (command.equals("")) {
            commandExecutor.execute(sender, env);
            return;
        }
        // Duplicate the environment
        env = new HashMap<>(env);
        // Get the key/alias and subcommand
        String[] split = command.split(" ", 2);
        String key = split[0];
        String subcommand = split[1];
        // Add any parameters to the environment
        Scanner scanner = new Scanner(subcommand);
        int i;
        for (i = 0; i < paramList.size(); i++) {
            String token = scanner.next();
            if (childAliases.containsKey(token)) {
                break;
            }
            env.put(paramList.get(i), scanner.next());
        }
        if (i < paramList.size()) {
            // TODO Handle incorrect number of params
        }
        scanner.close();
        // Add remaining tokens to the subcommand
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) {
            builder.append(scanner.next());
        }
        subcommand = builder.toString();
        // Call the subcommand on the child node
        get(key).execute(sender, subcommand, env);
    }
}
