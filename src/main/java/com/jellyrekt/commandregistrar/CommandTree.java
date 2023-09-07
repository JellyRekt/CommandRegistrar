package com.jellyrekt.commandregistrar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CommandTree extends CommandTreeNode {
    private String rootCommand;
    private JavaPlugin plugin;
    private CommandListener commandListener = new CommandListener(this);

    public CommandTree(String rootCommand, JavaPlugin plugin) {
        super();
        // TODO
        // Validate that rootCommand contains only one token
        this.rootCommand = rootCommand;
        this.plugin = plugin;
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
        super.execute(sender, stripExtraSpaces(command), new HashMap<>());
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

    String stripExtraSpaces(String s) {
        return s.trim().replaceAll(" +", " ");
    }
}
