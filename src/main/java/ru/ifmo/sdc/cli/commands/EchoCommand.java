package ru.ifmo.sdc.cli.commands;

import java.util.List;

public class EchoCommand extends Command {
    public EchoCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        // TODO: rewrite quotes handle
        return args.stream().limit(args.size() - 1).skip(2).reduce("", (x, y) -> x + y);
    }
}
