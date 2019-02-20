package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Parser {
    List<Command> parse(String line) {
        String[] rawCommands = line.split("\\|");
        return Stream.of(rawCommands).map(this::lineToCommand).collect(Collectors.toList());
    }

    private Command lineToCommand(String line) {
        String[] commandWithArgs = line.trim().split(" ", 2);
        String commandName = commandWithArgs[0];
        String args = commandWithArgs.length == 2 ? commandWithArgs[1] : "";
        switch (commandName) {
            case "cat": {
                return new CatCommand(args);
            }
            case "echo": {
                return new EchoCommand(args);
            }
            case "wc": {
                return new WcCommand(args);
            }
            case "pwd": {
                return new PwdCommand(args);
            }
            default: {

            }
        }
        return null;
    }
}
