package com.jellyrekt.commandregistrar;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CommandTree extends CommandTreeNode {
    private JavaPlugin plugin;
    private CommandListener commandListener = new CommandListener(this);

    private static Set<CommandTree> registry = new HashSet<>();

    public CommandTree(JavaPlugin plugin) {
        super();
        this.plugin = plugin;
        registry.add(this);
    }

    /**
     * Register a command.
     *
     * @param command     Full command string
     * @param executor    CommandExecutor to handle the command
     */
    @Override
    public void add(String command, CommandExecutor executor) {
        super.add(stripExtraSpaces(command), executor);
    }

    /**
     * Execute a command.
     *
     * @param sender
     * @param command
     */
    public void execute(CommandSender sender, String command) {
        super.execute(sender, command, new HashMap<>());
    }

    public void register() {
        // Register the event listener
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(commandListener, plugin);
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
