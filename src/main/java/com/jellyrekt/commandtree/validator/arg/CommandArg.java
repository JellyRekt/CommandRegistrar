package com.jellyrekt.commandtree.validator.arg;

import java.util.Map;

public interface CommandArg<T> {
    /**
     * Validate the argument in the unvalidated Env and return the validated result.
     * @param env Environment map
     * @return Validated argument
     */
    public T validate(Map<String, String[]> env);
}
