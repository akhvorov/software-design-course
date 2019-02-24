package ru.ifmo.sdc.cli.commands;

import java.util.List;

/**
 * Pwd command. Print current directory
 */
public class PwdCommand extends Command {
    public PwdCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        return System.getProperty("user.dir");
    }
}
