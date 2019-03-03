package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parser from line to commands
 */
class Parser {
    private static final CommandFactory commandFactory = new CommandFactory();

    /**
     * Make pipeline from command string
     *
     * @param line        string of commands
     * @param environment global environment with variables
     * @return list of commands in pipeline
     */
    public static List<Command> parse(String line, Environment environment) {
        final String substitutedLine = environment.substitute(line);
        List<String> tokens = tokenize(substitutedLine);
//        tokens = substitute(tokens, environment);
        return groupByCommand(tokens).stream().map(commandFactory::getCommand).collect(Collectors.toList());
    }

    /**
     * Split string on tokens
     *
     * @param line the command line to process
     * @return list of tokens in line
     */
    private static List<String> tokenize(String line) {
        if (line == null || line.length() == 0) {
            return new ArrayList<>();
        }
        final int ordinary = 0;
        final int inWeakQuote = 1;
        final int inStrongQuote = 2;
        int state = ordinary;
        final StringTokenizer tok = new StringTokenizer(line, "\"\' |", true);
        final List<String> result = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenIsQuoted = false;

        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            switch (state) {
                case inWeakQuote:
                    if (token.equals("\'")) {
                        lastTokenIsQuoted = true;
                        state = ordinary;
                    } else {
                        current.append(token);
                    }
                    break;
                case inStrongQuote:
                    if (token.equals("\"")) {
                        lastTokenIsQuoted = true;
                        state = ordinary;
                    } else {
                        current.append(token);
                    }
                    break;
                default:
                    switch (token) {
                        case "\'":
                            state = inWeakQuote;
                            break;
                        case "\"":
                            state = inStrongQuote;
                            break;
                        case " ":
                            if (lastTokenIsQuoted || current.length() != 0) {
                                result.add(current.toString());
                                current.setLength(0);
                            }
                            break;
                        case "|":
                            if (current.length() != 0) {
                                result.add(current.toString());
                                current.setLength(0);
                            }
                            result.add("|");
                            break;
                        default:
                            current.append(token);
                            break;
                    }
                    lastTokenIsQuoted = false;
                    break;
            }
        }
        if (lastTokenIsQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        return result;
    }

    /**
     * Group tokens by command
     *
     * @param tokens list of tokens
     * @return list of commands groups
     */
    private static List<List<String>> groupByCommand(final List<String> tokens) {
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
