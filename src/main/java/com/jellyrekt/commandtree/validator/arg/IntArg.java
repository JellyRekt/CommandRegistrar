package com.jellyrekt.commandtree.validator.arg;

import java.util.Map;

/**
 * Validate an Integer argument.
 */
public class IntArg extends AbstractCommandArg<Integer> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    /**
     * @inheritdoc
     */
    public IntArg(String key, int index, String name) {
        super(key, index, name);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Integer validate(Map<String, String[]> env) {
        // TODO validate min and max
        return Integer.parseInt(env.get(getKey())[getIndex()]);
    }

    /**
     * Set the minimum value for this argument.
     * @param min Minimum value for this argument.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Set the maximum value for this argument.
     * @param max Maximum value for this argument.
     */
    public void setMax(int max) {
        this.max = max;
    }
}
