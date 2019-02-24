package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Cat command. Show content of file
 */
public class CatCommand extends Command {
    public CatCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        String text;
        if (args.size() > 2) {
            try {
                text = new String(Files.readAllBytes(Paths.get(args.get(2)))).trim();
            } catch (IOException e) {
                System.err.println("Can't read file");
                throw new IOException(e);
            }
        } else {
            text = prevResult;
        }
        return text;
    }
}
