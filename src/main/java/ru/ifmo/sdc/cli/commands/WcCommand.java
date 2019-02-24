package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Wc command. Show lines, words and bytes in provided file or text
 */
public class WcCommand extends Command {
    public WcCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        String text;
        int bytes = 0;
        if (args.size() > 2) {
            try {
                text = new String(Files.readAllBytes(Paths.get(args.get(2))));
            } catch (IOException e) {
                System.err.println("Can't read file");
                throw new IOException(e);
            }
        } else {
            text = prevResult;
            bytes++;
        }
        int lines = text.split("\n").length;
        int words = (int) Arrays.stream(text.split("[ \n\t\r]")).filter(x -> !x.isEmpty() && !x.equals(" ")).count();
        bytes += text.getBytes().length;
        return String.format("%d %d %d", lines, words, bytes);
    }
}
