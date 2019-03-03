package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parser from line to commands
 */
class Parser {
    private static final Set<Character> SPLITERS_SET = new HashSet<>(Arrays.asList('\'', '"', '|', '=', '$'));
    private static final CommandFactory commandFactory = new CommandFactory();

    /**
     * Make pipeline from command string
     *
     * @param line        string of commands
     * @param environment global environment with variables
     * @return list of commands in pipeline
     */
    public List<Command> parse(String line, Environment environment) {
//        List<String> tokens = substitute(tokenize(line), environment);
        String substitutedLine = environment.substitute(line);
        List<String> tokens = substitute(tokenize(substitutedLine), environment);
        return groupByCommand(tokens).stream().map(commandFactory::getCommand).collect(Collectors.toList());
    }

    /**
     * Split line on tokens
     *
     * @param line line with commands
     * @return list of tokens
     */
    private List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        char quotesType;
        for (int i = 0; i < line.length(); i++) {
            char symbol = line.charAt(i);
            if (symbol == ' ') {
                String currentToken = sb.toString().trim();
                if (!currentToken.isEmpty()) {
                    tokens.add(currentToken);
                    sb = new StringBuilder();
                }
                tokens.add(" ");
                while (i < line.length() && line.charAt(i) == ' ') {
                    i++;
                }
                i--;
            } else if (symbol == '\'' || symbol == '"') {
                if (!sb.toString().isEmpty()) {
                    tokens.add(sb.toString());
                    sb = new StringBuilder();
                }
                quotesType = symbol;
                tokens.add(String.valueOf(quotesType));
                i++;
                while (i < line.length() && line.charAt(i) != quotesType) {
                    sb.append(line.charAt(i));
                    i++;
                }
                tokens.add(sb.toString());
                sb = new StringBuilder();
                tokens.add(String.valueOf(quotesType));
            } else if (SPLITERS_SET.contains(symbol)) {
                if (!sb.toString().isEmpty()) {
                    tokens.add(sb.toString());
                    sb = new StringBuilder();
                }
                tokens.add(String.valueOf(symbol));
            } else {
                sb.append(symbol);
            }
        }
        if (!sb.toString().isEmpty()) {
            tokens.add(sb.toString());
        }
        return tokens;
    }

    /**
     * Make substitution of variables or make assigning
     *
     * @param tokens      list of tokens
     * @param environment environment with variables
     * @return list of tokens after substitution
     */
    private List<String> substitute(List<String> tokens, Environment environment) {
        List<String> substTokens = new ArrayList<>();
        int weakBreakNum = 0;
        int strongBreakNum = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("\"")) {
                strongBreakNum ^= 1;
            }
            if (token.equals("'")) {
                weakBreakNum ^= 1;
            }
            if (token.equals("=") && weakBreakNum != 1 && strongBreakNum != 1) {
                substTokens.remove(substTokens.size() - 1);
                environment.put(tokens.get(i - 1), tokens.get(i + 1));
                i++;
                continue;
            }
            if (!token.isEmpty()) {
                substTokens.add(token);
            }
        }
        return substTokens;
    }

    /**
     * Group tokens by command
     *
     * @param tokens list of tokens
     * @return list of commands groups
     */
    private List<List<String>> groupByCommand(List<String> tokens) {
        List<List<String>> commandGroups = new ArrayList<>();
        List<String> currentCommand = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("|")) {
                if (tokens.get(i - 1).equals(" ")) {
                    currentCommand.remove(currentCommand.size() - 1);
                }
                commandGroups.add(currentCommand);
                currentCommand = new ArrayList<>();
                if (tokens.get(i + 1).equals(" ")) {
                    i++;
                }
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
