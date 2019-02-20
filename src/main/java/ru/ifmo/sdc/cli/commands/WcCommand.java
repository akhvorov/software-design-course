package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WcCommand extends Command {
    public WcCommand(String args) {
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
        int lines = text.split("\n").length;
        int words = text.split(" ").length;
        int bytes = text.getBytes().length;
        return String.format("%d %d %d", lines, words, bytes);
    }
}
