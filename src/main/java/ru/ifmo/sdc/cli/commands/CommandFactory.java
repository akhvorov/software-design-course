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
    public Command getCommand(List<String> tokens) {
        String[] args = tokens.toArray(new String[0]);
        String firstToken = tokens.get(0);
        Command command;
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
            command = new CatCommand();
        } else if (tokens.size() == 1 && tokens.get(0).contains("=")) {
            command = new AssignCommand();
        } else {
            command = new ExternalCommand();
        }
        return CommandLine.populateCommand(command, args);
    }
}
