package ru.ifmo.sdc.cli.commands;

import java.util.List;

public class EchoCommand extends Command {
    public EchoCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        return tokensToString(args.subList(1, args.size()));
    }
}
