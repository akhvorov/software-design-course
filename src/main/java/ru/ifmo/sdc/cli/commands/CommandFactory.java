package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;

import java.util.List;

/**
 * Create commands
 */
public class CommandFactory {
    /**
     * Create command by tokens
     *
     * @param tokens list of tokens
     * @return command
     */
    public static Command getCommand(final List<String> tokens) {
        final String[] args = tokens.toArray(new String[0]);
        final String firstToken = tokens.get(0);
        final Command command;
        if (firstToken.equals("pwd")) {
            command = new PwdCommand();
        } else if (firstToken.equals("echo")) {
            command = new EchoCommand();
        } else if (firstToken.equals("wc")) {
            command = new WcCommand();
        } else if (firstToken.equals("cat")) {
            command = new CatCommand();
        } else if (firstToken.equals("exit")) {
            command = new ExitCommand();
        } else if (firstToken.equals("grep")) {
            command = new GrepCommand();
        } else if (tokens.size() == 1 && tokens.get(0).contains("=")) {
            command = new AssignCommand();
        } else {
            return CommandLine.populateCommand(new ExternalCommand(args), args[0]);
        }
        return CommandLine.populateCommand(command, args);
    }
}
