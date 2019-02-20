package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatCommand extends Command {
    public CatCommand(String args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        String text;
        if (!args.equals("")) {
            text = new String(Files.readAllBytes(Paths.get(args)));
        } else {
            text = prevResult;
        }
        return text;
    }
}
