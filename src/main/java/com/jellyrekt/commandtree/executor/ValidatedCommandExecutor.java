package com.jellyrekt.commandtree.executor;

import com.jellyrekt.commandtree.arg.AbstractCommandArg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ValidatedCommandExecutor implements CommandExecutor<Object> {
    private Map<String, List<AbstractCommandArg>> args = new HashMap<>();

    public Map<String, Object> validate(Map<String, String[]> env) {
        Map<String, Object> result = new HashMap<>();
        for (String key : args.keySet()) {
            List<AbstractCommandArg> argList = args.get(key);
            for (int i = 0; i < argList.size(); i++) {
                AbstractCommandArg arg = argList.get(i);
                result.put(arg.getName(), arg.validate(env.get(key)[i]));
            }
        }
        return result;
    }

    public ValidatedCommandExecutor withArgs(AbstractCommandArg... args) {
        for (AbstractCommandArg arg: args) {
            addArg(arg);
        }
        return this;
    }

    private void addArg(AbstractCommandArg arg) {
        if (!this.args.keySet().contains(arg.getKey())) {
            this.args.put(arg.getKey(), new ArrayList<>());
        }
        args.get(arg.getKey()).add(arg);
    }

}
