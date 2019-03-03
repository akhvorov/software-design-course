package ru.ifmo.sdc.cli.commands;

import ru.ifmo.sdc.cli.Environment;

/**
 * Pwd command. Print current directory
 */
public class PwdCommand extends Command {

    @Override
    public String execute(String prevResult, Environment environment) {
        return System.getProperty("user.dir");
    }
}
