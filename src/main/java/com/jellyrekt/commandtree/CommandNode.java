package com.jellyrekt.commandtree;

import org.bukkit.command.CommandSender;

import java.util.*;

public class CommandNode {
    /**
     * Nodes containing the subcommand of this node's command
     */
    private Map<String, CommandNode> children = new HashMap<>();
    /**
     * Executor to handle the command contained in this node
     */
    private CommandExecutor commandExecutor;
    /**
     * Permission needed to execute this command
     */
    private String permission = null;

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
            children.put(key, new CommandNode());
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
