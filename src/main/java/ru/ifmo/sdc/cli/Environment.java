package ru.ifmo.sdc.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment for save variables values
 */
public class Environment {
    private final Map<String, String> variables = new HashMap<>();

    /**
     * Get value of variable
     *
     * @param key variable name
     * @return variable value
     */
    public String get(final String key) {
        return variables.get(key);
    }

    /**
     * Save value of variable
     *
     * @param key   variable name
     * @param value variable value
     */
    public void put(final String key, final String value) {
        variables.put(key, value);
    }
}
