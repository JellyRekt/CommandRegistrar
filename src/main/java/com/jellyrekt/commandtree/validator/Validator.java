package com.jellyrekt.commandtree.validator;

import com.jellyrekt.commandtree.validator.arg.AbstractCommandArg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {
    private Map<String, List<AbstractCommandArg>> args = new HashMap<>();

    public Map<String, Object> validate(Map<String, String[]> env) {
        Map<String, Object> result = new HashMap<>();
        for (String key : args.keySet()) {
            for (AbstractCommandArg arg : args.get(key)) {
                result.put(arg.getName(), arg.validate(env));
            }
        }
        return result;
    }

    public void addArg(AbstractCommandArg arg) {
        if (!this.args.keySet().contains(arg.getKey())) {
            this.args.put(arg.getKey(), new ArrayList<>());
        }
        args.get(arg.getKey()).add(arg);
    }
}
