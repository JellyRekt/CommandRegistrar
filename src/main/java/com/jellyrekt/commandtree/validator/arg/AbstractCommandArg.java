package com.jellyrekt.commandtree.validator.arg;

public abstract class AbstractCommandArg<T> implements CommandArg<T> {
    /**
     * Key used to access the arg in the unvalidated Env map
     */
    private String key;
    /**
     * Name to use for the arg in the validated Env map
     */
    private String name;

    /**
     * @param key   Key used to access the arg in the unvalidated Env map
     * @param name  Name to use for the arg in the validated Env map
     */
    public AbstractCommandArg(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * @return Key used to access the arg in the unvalidated Env map
     */
    public String getKey() {
        return key;
    }

    /**
     * @return Name to use for the arg in the validated Env map
     */
    public String getName() {
        return name;
    }
}
