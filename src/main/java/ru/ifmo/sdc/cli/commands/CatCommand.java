package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CatCommand extends Command {
    public CatCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        String text;
        if (args.size() > 1) {
            text = new String(Files.readAllBytes(Paths.get(args.get(1))));
        } else {
            text = prevResult;
        }
        return text;
    }
}
