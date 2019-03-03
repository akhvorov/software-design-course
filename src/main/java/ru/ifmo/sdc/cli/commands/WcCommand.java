package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wc command. Show lines, words and bytes in provided file or text
 */
public class WcCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> params = new ArrayList<>();

    @Override
    public String execute(String prevResult, Environment environment) throws IOException {
        String text;
        int bytes = 0;
        if (params.size() > 0) {
            try {
                text = new String(Files.readAllBytes(Paths.get(params.get(0))));
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
