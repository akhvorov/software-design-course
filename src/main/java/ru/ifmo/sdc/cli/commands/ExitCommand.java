package ru.ifmo.sdc.cli.commands;

import ru.ifmo.sdc.cli.Environment;

/**
 * Exit command. Flag for termination
 */
public class ExitCommand extends Command {
    @Override
    public String execute(String prevResult, Environment environment) {
        throw new UnsupportedOperationException();
    }

    public boolean isTerminate() {
        return true;
    }
}
