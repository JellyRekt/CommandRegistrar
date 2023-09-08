package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;

import java.util.*;

class CommandTreeNode {
    /**
     * Nodes containing the subcommand of this node's command
     */
    private Map<String, CommandTreeNode> children = new HashMap<>();
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
     * Register a subcommand under this command.
     *
     * @param subcommand  Key (first token) of the subcommand
     * @param description Description for the subcommand
     * @param usage       Usage message for the subcommand
     * @param executor    CommandExecutor to handle the command
     */
    void add(String subcommand, String description, String usage, CommandExecutor executor) {
        // Base case: Emptry string
        if (subcommand.isEmpty()) {
            this.description = description;
            this.usage = usage;
            this.commandExecutor = executor;
            return;
        }
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
            if (!isParam(next)) {
                break;
            }
            // Trim off the : and add to the param list.
            paramList.add(extractParamKey(next));
        }
        // If the end of the string hasn't been reached
        if (!scanner.hasNext()) {
            next = "";
        }
        // Turn the remainder of the string into the subcommand
        StringBuilder builder = new StringBuilder(next + " ");
        while (scanner.hasNext()) {
            builder.append(scanner.next()).append(" ");
        }
        scanner.close();
        subcommand = builder.toString().trim();
        // Pass the rest of the work to the child node
        CommandTreeNode node = children.get(key);
        // Create a child if it doesn't already exist
        // (It probably doesn't, but this way commands don't have to be defined in order)
        if (node == null) {
            children.put(key, new CommandTreeNode());
            node = children.get(key);
        }
        // Recursive call
        node.add(subcommand, description, usage, executor);
    }

    /**
     * Execute the given command
     *
     * @param sender
     * @param command
     * @param env
     */
    protected void execute(CommandSender sender, String command, Map<String, String> env) {
        System.out.println("Command: " + command);
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
        String token = "";
        for (i = 0; i < paramList.size(); i++) {
            token = scanner.next();
            // Stop parsing args if a symbol has been reached
            if (children.containsKey(token)) {
                break;
            }
            env.put(paramList.get(i), token);
        }
        if (i < paramList.size()) {
            // TODO Handle incorrect number of params
        }
        // If the end of the string hasn't been reached
        if (!scanner.hasNext()) {
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
        children.get(key).execute(sender, subcommand, env);
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

    protected Map<String, CommandTreeNode> getChildren() {
        return children;
    }

    protected String toStringRec(String key, int depth) {
        StringBuilder builder = new StringBuilder("\n");
        for (int i = 0; i < depth; i++) {
            builder.append("| ");
        }
        builder.append(key);
        for (String param : paramList) {
            builder.append(" :").append(param);
        }
        for (String k : children.keySet()) {
            builder.append(children.get(k).toStringRec(k, depth + 1));
        }
        return builder.toString();
    }
}
