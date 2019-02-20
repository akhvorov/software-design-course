package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.*;

import java.util.*;
import java.util.stream.Collectors;

class Parser {
    private static final Set<Character> SPLITERS_SET = new HashSet<>(Arrays.asList('\'', '"', '|', '=', '$'));
    private static final CommandFactory commandFactory = new CommandFactory();

    List<Command> parse(String line, Environment environment) {
        List<String> tokens = substitude(tokenize(line), environment);
        System.out.println(tokens);
        return groupByCommand(tokens).stream().map(commandFactory::getCommand).collect(Collectors.toList());
    }

    private List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char symbol = line.charAt(i);
            if (symbol == ' ') {
                if (!sb.toString().isEmpty()) {
                    tokens.add(sb.toString());
                    sb = new StringBuilder();
                }
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

    private List<String> substitude(List<String> tokens, Environment environment) {
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
            if (token.equals("$") && weakBreakNum != 1) {
                substTokens.add(environment.get(tokens.get(i + 1)));
                i++;
                continue;
            }
            if (token.equals("=") && weakBreakNum != 1  && strongBreakNum != 1) {
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

    private List<List<String>> groupByCommand(List<String> tokens) {
        List<List<String>> commandGroups = new ArrayList<>();
        List<String> currentCommand = new ArrayList<>();
        for (String token : tokens) {
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
