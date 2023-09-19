package com.jellyrekt.commandtree.arg;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerArg extends AbstractCommandArg<Player> {
    JavaPlugin plugin;

    /**
     * @param key  Key used to access the arg in the unvalidated Env map
     * @param name Name to use for the arg in the validated Env map
     */
    public PlayerArg(String key, String name, JavaPlugin plugin) {
        super(key, name);
        this.plugin = plugin;
    }

    @Override public Player validate(String arg) {
        return plugin.getServer().getPlayer(arg);
    }
}
