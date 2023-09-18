package com.jellyrekt.commandtree;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CommandTree extends CommandNode {
    /**
     * Plugin registering the commands
     */
    private JavaPlugin plugin;
    /**
     * EventListener in charge of executing the commands
     */
    private CommandListener commandListener = new CommandListener(this);
    /**
     * Default permission denied message for commands under this tree
     */
    private String permissionDeniedMessage = "§4Insufficient permission.";

    /**
     * Create a CommandTree to register commands to the plugin.
     *
     * @param plugin Plugin registering the commands.
     */
    public CommandTree(JavaPlugin plugin) {
        super(null, null);
        this.plugin = plugin;
    }

    /**
     * Register a command.
     *
     * @param command  Full command string
     */
    @Override
    public CommandNode add(String command) {
        return super
                .add(stripExtraSpaces(command))
                .setPermissionDeniedMessage(permissionDeniedMessage);
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

    /**
     * Register the commands in this tree to the plugin.
     * Root commands still need to be registered in plugin.yml.
     */
    public void register() {
        // Register the event listener
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(commandListener, plugin);
    }

    /**
     * Set the default permission denied message for commands registered to this tree.
     * Does not affect nodes that are already on this tree.
     * @param message Permission denied message
     * @return self
     */
    public CommandTree setPermissionDeniedMessage(String message) {
        permissionDeniedMessage = message;
        return this;
    }

    /**
     * Trim the string and remove all repeated spaces.
     *
     * @param s String to fix.
     * @return The passed string, with leading, trailing, and repeated spaces removed.
     */
    static String stripExtraSpaces(String s) {
        return s.trim().replaceAll(" +", " ");
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        for (String key : getChildren().keySet()) {
            builder.append(getChildren().get(key).toStringRec(key, 0));
        }
        return builder.append("\n").toString();
    }
}
