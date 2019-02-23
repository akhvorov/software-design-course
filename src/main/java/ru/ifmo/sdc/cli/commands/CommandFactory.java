package ru.ifmo.sdc.cli.commands;

import java.util.List;

public class CommandFactory {
    public Command getCommand(List<String> tokens) {
        String firstToken = tokens.get(0);
        if (firstToken.equals("pwd")) {
            return new PwdCommand(tokens);
        } else if (firstToken.equals("echo")) {
            return new EchoCommand(tokens);
        } else if (firstToken.equals("wc")) {
            return new WcCommand(tokens);
        } else if (firstToken.equals("cat")) {
            return new CatCommand(tokens);
        } else if (firstToken.equals("exit")) {
            return new ExitCommand(tokens);
        } else if (tokens.size() > 1 && tokens.get(1).equals("=")) {
            return new AssignCommand(tokens);
        } else {
            return new ExternalCommand(tokens);
        }
    }
}
