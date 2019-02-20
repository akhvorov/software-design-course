package ru.ifmo.sdc.cli.commands;

import java.io.File;

public class PwdCommand extends Command {
    public PwdCommand(String args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        return new File(args).getAbsolutePath();
    }
}
