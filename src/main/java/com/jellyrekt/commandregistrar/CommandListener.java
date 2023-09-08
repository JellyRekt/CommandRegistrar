package com.jellyrekt.commandregistrar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
public class CommandListener implements Listener {
    private CommandTree tree;

    CommandListener(CommandTree tree) {
        this.tree = tree;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = CommandTree.stripExtraSpaces(event.getCommand());
        if (!isCommand(command)) {
            return;
        }
        tree.execute(event.getSender(), command);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = CommandTree.stripExtraSpaces(event.getMessage());
        if (!isCommand(command)) {
            return;
        }
        tree.execute(event.getPlayer(), command);
    }

    private boolean isCommand(String message) {
        // TODO probably more logic needed than this
        return message.split(" ", 2)[0].equals(tree.getRootCommand());
    }
}
