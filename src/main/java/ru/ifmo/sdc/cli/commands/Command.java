package ru.ifmo.sdc.cli.commands;

import java.io.IOException;

public abstract class Command {
    protected final String args;

    public Command(String args) {
        this.args = args.trim();
    }

    public abstract String execute(String prevResult) throws IOException;
}
