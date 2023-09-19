package com.jellyrekt.commandtree.validator.arg;

public abstract class AbstractCommandArg<T> implements CommandArg<T> {
    /**
     * Key used to access the arg in the unvalidated Env map
     */
    private String key;
    /**
     * Index used to access the arg in the unvalidated Env map
     */
    private int index;
    /**
     * Name to use for the arg in the validated Env map
     */
    private String name;

    /**
     * @param key   Key used to access the arg in the unvalidated Env map
     * @param index Index used to access the arg in the unvalidated Env map
     * @param name  Name to use for the arg in the validated Env map
     */
    public AbstractCommandArg(String key, int index, String name) {
        this.key = key;
        this.index = index;
        this.name = name;
    }

    /**
     * @return Key used to access the arg in the unvalidated Env map
     */
    protected String getKey() {
        return key;
    }

    /**
     * @return Index used to access the arg in the unvalidated Env map
     */
    protected int getIndex() {
        return index;
    }

    /**
     * @return Name to use for the arg in the validated Env map
     */
    protected String getName() {
        return name;
    }
}
