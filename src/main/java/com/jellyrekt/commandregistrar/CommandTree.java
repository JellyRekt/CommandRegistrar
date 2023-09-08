package com.jellyrekt.commandregistrar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CommandTree extends CommandTreeNode {
    private String rootCommand;
    private JavaPlugin plugin;
    private CommandListener commandListener = new CommandListener(this);

    private static Set<CommandTree> registry = new HashSet<>();

    public CommandTree(String rootCommand, JavaPlugin plugin) {
        super(new ArrayList<>());
        // TODO
        // Validate that rootCommand contains only one token
        this.rootCommand = rootCommand;
        this.plugin = plugin;
        registry.add(this);
    }

    /**
     * Register a command.
     *
     * @param command     Full command string
     * @param description Description for the command
     * @param usage       Usage message for the command
     * @param executor    CommandExecutor to handle the command
     */
    @Override
    public void add(String command, String description, String usage, CommandExecutor executor) {
        super.add(stripExtraSpaces(command), description, usage, executor);
    }

    /**
     * Execute a command.
     *
     * @param sender
     * @param command
     */
    public void execute(CommandSender sender, String command) {
        try {
            // TODO this will probably cause an error if there's no subcommand provided
            getChildren().get(rootCommand).execute(sender, stripExtraSpaces(command).split(" ", 2)[1], new HashMap<>());
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public void register() {
        // Register the root command with the plugin
        plugin
                .getCommand(rootCommand)
                .setExecutor(new org.bukkit.command.CommandExecutor() {
                    @Override public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
                        return true;
                    }
                });
        // Register the event listener
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(commandListener, plugin);
    }

    protected String getRootCommand() {
        return rootCommand;
    }

    protected JavaPlugin getPlugin() {
        return plugin;
    }

    static String stripExtraSpaces(String s) {
        return s.trim().replaceAll(" +", " ");
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        for (String key : getChildren().keySet()) {
            builder.append(getChildren().get(key).toStringRec(key, 0));
        }
        return builder.append("\n").toString();
    }
}
