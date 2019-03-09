package ru.ifmo.sdc.cli.commands;

import ru.ifmo.sdc.cli.Environment;

/**
 * Pwd command. Print current directory
 */
public class PwdCommand extends Command {

    @Override
    public String execute(final String prevResult, final Environment environment) {
        return environment.get("PWD") + '\n';
    }
}
