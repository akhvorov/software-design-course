package ru.ifmo.sdc.cli.commands;

public class EchoCommand extends Command {
    public EchoCommand(String args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        return args;
    }
}
