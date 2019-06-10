package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parser from line to commands
 */
class Parser {
    /**
     * Make pipeline from command string
     *
     * @param line        string of commands
     * @param environment global environment with variables
     * @return list of commands in the pipeline
     */
    public static List<Command> parse(final String line, final Environment environment) {
        final List<String> tokens = new Tokenizer(substitute(environment, line)).tokens();
        return splitByPipe(tokens).stream().map(CommandFactory::getCommand).collect(Collectors.toList());
    }

    /**
     * Substitute variables values to string
     *
     * @param environment current environment
     * @param line line with commands
     * @return same line with substituted variables
     */
    public static String substitute(final Environment environment, final String line) {
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
                if (environment.contains(var.toString())) {
                    sb.append(environment.get(var.toString()));
                }
                continue;
            }
            sb.append(symbol);
        }
        return sb.toString();
    }

    /**
     * Split tokens by the pipe
     *
     * @param tokens list of tokens
     * @return list of pipe-split tokens
     */
    private static List<List<String>> splitByPipe(final List<String> tokens) {
        final List<List<String>> commandGroups = new ArrayList<>();
        List<String> currentCommand = new ArrayList<>();
        for (final String token : tokens) {
            if (token.equals("|")) {
                commandGroups.add(currentCommand);
                currentCommand = new ArrayList<>();
                continue;
            }
            currentCommand.add(token);
        }
        if (!currentCommand.isEmpty()) {
            commandGroups.add(currentCommand);
        }
        return commandGroups;
    }
}
