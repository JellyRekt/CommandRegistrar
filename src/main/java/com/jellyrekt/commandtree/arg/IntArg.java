package com.jellyrekt.commandtree.arg;

/**
 * Validate an Integer argument.
 */
public class IntArg extends AbstractCommandArg<Integer> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    /**
     * @inheritdoc
     */
    public IntArg(String key, String name) {
        super(key, name);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Integer validate(String arg) {
        // TODO validate min and max
        return Integer.parseInt(arg);
    }

    /**
     * Set the minimum value for this argument.
     * @param min Minimum value for this argument.
     */
    public IntArg setMin(int min) {
        this.min = min;
        return this;
    }

    /**
     * Set the maximum value for this argument.
     * @param max Maximum value for this argument.
     */
    public IntArg setMax(int max) {
        this.max = max;
        return this;
    }
}
