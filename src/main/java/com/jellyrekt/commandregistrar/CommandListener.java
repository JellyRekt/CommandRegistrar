package com.jellyrekt.commandregistrar;

import org.bukkit.event.Event;
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
        tree.getPlugin().getLogger().info("Console executed " + event.getCommand());
        // TODO
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        tree.getPlugin().getLogger().info("Player executed " + event.getMessage());
        // TODO
    }

    private void handle(Event event) {
        // TODO
    }
}
