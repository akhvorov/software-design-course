package ru.ifmo.sdc.cli;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment for save variables values
 */
public class Environment {
    public Environment(Map<String, String> variables, String userDir) {
        this.variables = new HashMap<>(variables);
        this.userDir = userDir;
    }

    private final Map<String, String> variables;
    public String userDir;

    /**
     * Substitute variables values to string
     *
     * @param line line with commands
     * @return same line with substituted variables
     */
    public String substitute(final String line) {
        final StringBuilder sb = new StringBuilder();
        boolean inStrongQuotes = false;
        boolean inWeakQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            final char symbol = line.charAt(i);
            if (symbol == '\'' && !inStrongQuotes) {
                inWeakQuotes ^= true;
            }
            if (symbol == '"' && !inWeakQuotes) {
                inStrongQuotes ^= true;
            }
            if (symbol == '$' && !inWeakQuotes) {
                i++;
                char tmpSymbol = line.charAt(i);
                final StringBuilder var = new StringBuilder();
                while (Character.isAlphabetic(line.charAt(i)) || Character.isDigit(symbol)) {
                    var.append(tmpSymbol);
                    i++;
                    if (i == line.length()) {
                        break;
                    }
                    tmpSymbol = line.charAt(i);
                }
                i--;
                sb.append(variables.getOrDefault(var.toString(), ""));
                continue;
            }
            sb.append(symbol);
        }
        return sb.toString();
    }

    public String get(final String key) {
        return variables.getOrDefault(key, "");
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
