package com.jellyrekt.commandtree.arg;

import org.bukkit.Material;

public class MaterialArg extends AbstractCommandArg<Material> {

    /**
     * @param key  Key used to access the arg in the unvalidated Env map
     * @param name Name to use for the arg in the validated Env map
     */
    public MaterialArg(String key, String name) {
        super(key, name);
    }

    @Override public Material validate(String arg) {
        // TODO handle null (material not found)
        return Material.matchMaterial(arg);
    }
}
