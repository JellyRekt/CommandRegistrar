package com.jellyrekt.commandregistrar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class CommandTree extends CommandTreeNode {
    private String rootCommand;
    private JavaPlugin plugin;
    private CommandListener commandListener = new CommandListener(this);

    public CommandTree(String rootCommand, JavaPlugin plugin) {
        super();
        this.rootCommand = rootCommand;
        this.plugin = plugin;
    }

    /**
     * Register a command.
     *
     * @param command     Full command string
     * @param aliases     Strings which are accepted as aliases for this command
     * @param description Description for the command
     * @param usage       Usage message for the command
     * @param executor    CommandExecutor to handle the command
     */
    @Override
    public void add(String command, Set<String> aliases, String description, String usage, CommandExecutor executor) {
        add(command, aliases, description, usage, executor);
    }

    /**
     * Execute a command.
     *
     * @param sender
     * @param command
     */
    public void execute(CommandSender sender, String command) {
        execute(sender, command, new HashMap<>());
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
}
