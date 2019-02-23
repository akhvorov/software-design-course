package ru.ifmo.sdc.cli.commands;

import java.util.List;

public class ExitCommand extends Command {
    public ExitCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        throw new UnsupportedOperationException();
    }

    public boolean isTerminate() {
        return true;
    }
}
