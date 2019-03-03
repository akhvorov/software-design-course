package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Cat command. Show content of file
 */
public class CatCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> params = new ArrayList<>();

    @Override
    public String execute(String prevResult, Environment environment) throws IOException {
        String text;
        if (params.size() > 0) {
            try {
                text = new String(Files.readAllBytes(Paths.get(params.get(0))));
                text = text.substring(0, text.length() - 1);
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
