package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WcCommand extends Command {
    public WcCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        String text;
        if (args.size() > 2) {
            text = new String(Files.readAllBytes(Paths.get(args.get(2))));
        } else {
            text = prevResult;
        }
        int lines = text.split("\n").length;
        int words = text.split(" ").length + 1;
        int bytes = text.getBytes().length;
        return String.format("%d %d %d", lines, words, bytes);
    }
}
