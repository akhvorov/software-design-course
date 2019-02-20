package ru.ifmo.sdc.cli;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, String> variables = new HashMap<>();

    public String get(final String key) {
        return variables.get(key);
    }

    public void put(final String key, final String value) {
        variables.put(key, value);
    }
}
