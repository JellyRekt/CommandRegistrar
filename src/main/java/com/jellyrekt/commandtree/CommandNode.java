package com.jellyrekt.commandtree;

import org.bukkit.command.CommandSender;

import java.util.*;

public class CommandNode {
    /**
     * Nodes containing the subcommand of this node's command
     */
    private Map<String, CommandNode> children = new HashMap<>();
    /**
     * Maps an alias to a child node's key
     */
    private Map<String, String> childAliases = new HashMap<>();
    /**
     * Executor to handle the command contained in this node
     */
    private CommandExecutor commandExecutor;
    /**
     * Permission needed to execute this command
     */
    private String permission = null;
    /**
     * Message sent when sender does not have permission to execute this command
     */
    private String permissionDeniedMessage;
    /**
     * Parent of this command node
     * @param parent
     */
    private CommandNode parent;

    /**
     * Create a command node.
     * @param parent Parent of this command node
     */
    protected CommandNode(CommandNode parent) {
        this.parent = parent;
    }

    /**
     * Set the permission needed to execute this command.
     * @param permission Permission needed to execute this command
     * @return self
     */
    public CommandNode setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Set the message sent when sender does not have permission to execute this command
     * @param message Message to send
     * @return self
     */
    public CommandNode setPermissionDeniedMessage(String message) {
        permissionDeniedMessage = message;
        return this;
    }

    /**
     * Add aliases for this command node.
     * Aliases are local to the level of the node;
     * for example, adding "b" as an alias to the command "alpha beta"
     * allows you to execute it with "alpha b".
     * @param aliases Aliases for this command.
     * @return self
     */
    public CommandNode addAliases(String... aliases) {
        for (String a : aliases) {
            parent.children.put(a, this);
        }
        return this;
    }

    /**
     * Register a subcommand under this command.
     *
     * @param subcommand  Key (first token) of the subcommand
     * @param executor    CommandExecutor to handle the command
     */
    CommandNode add(String subcommand, CommandExecutor executor) {
        // Base case: Empty string
        if (subcommand.isEmpty()) {
            this.commandExecutor = executor;
            return this;
        }
        // Consume the first token to use as a key
        String[] split = subcommand.split(" ", 2);
        String key = split[0];
        subcommand = split.length > 1 ? split[1] : "";
        // Pass the rest of the work to the child node
        CommandNode child = children.get(key);
        // Create a child if it doesn't already exist
        // (It probably doesn't, but this way commands don't have to be defined in order)
        if (child == null) {
            children.put(key, new CommandNode(this));
            child = children.get(key);
        }
        // Recursive call
        return child.add(subcommand, executor);
    }

    /**
     * Execute the given command
     *
     * @param sender
     * @param command
     * @param env
     */
    protected void execute(CommandSender sender, String command, Map<String, String[]> env) {
        // Base case: we've arrived at the final node
        if (command.isBlank()) {
            // Check for permission
            if (permission != null && !sender.hasPermission(permission)) {
                // TODO Allow developer to control how to handle this.
                sender.sendMessage(permissionDeniedMessage);
                return;
            }
            commandExecutor.execute(sender, env);
            return;
        }
        // Get the key/alias and subcommand
        String[] split = command.split(" ", 2);
        String key = split[0];
        String subcommand = split.length > 1 ? split[1] : "";
        // Easy-to-use ref to the next node to be called
        CommandNode child = children.get(key);
        // Add any arguments to the environment
        List<String> args = new ArrayList<>();
        Scanner scanner = new Scanner(subcommand);
        String token = "";
        while (scanner.hasNext()) {
            token = scanner.next();
            // Stop parsing args if a symbol has been reached
            if (child.children.containsKey(token)) {
                break;
            }
            args.add(token);
        }
        env.put(key, args.toArray(new String[0]));
        // If the end of the string hasn't been reached
        if (!child.children.containsKey(token)) {
            token = "";
        }
        // Turn the remainder of the string into the subcommand
        StringBuilder builder = new StringBuilder(token + " ");
        while (scanner.hasNext()) {
            builder.append(scanner.next()).append(" ");
        }
        scanner.close();
        subcommand = builder.toString().trim();
        // Call the subcommand on the child node
        child.execute(sender, subcommand, env);
    }

    /**
     * Gets a child node by key or alias.
     * @param key key or alias
     * @return CommandNode child node
     */
    private CommandNode getChild(String key) {
        return children.get(childAliases.get(key));
    }

    /**
     * Checks if a String matches the param pattern
     *
     * @param s String to check
     * @return True if the string matches the param pattern
     */
    private static boolean isParam(String s) {
        return s.charAt(0) == ':';
    }

    /**
     * Converts a param string to the key used for the environment
     *
     * @param s Param string (formatted according to rules)
     * @return The string used as a key in the environment
     */
    private static String extractParamKey(String s) {
        return s.substring(1);
    }

    protected Map<String, CommandNode> getChildren() {
        return children;
    }

    /**
     * Helper to build string representation of the full tree.
     * @param key This node's key
     * @param depth This node's depth in the command tree
     * @return String representation of this subtree
     */
    protected String toStringRec(String key, int depth) {
        StringBuilder builder = new StringBuilder("\n");
        builder.append("| ".repeat(depth));
        builder.append(key);
        for (String k : children.keySet()) {
            builder.append(children.get(k).toStringRec(k, depth + 1));
        }
        return builder.toString();
    }
}
