package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.util.List;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import ru.ifmo.sdc.cli.Environment;

public class GrepCommand extends Command {
    @Option(names = { "-i"}, description = "Insensitive to register")
    private boolean registerSensitive = false;

    @Option(names = { "-w"}, description = "Find whole word")
    private boolean wholeWord = false;

    @Option(names = { "-A"}, description = "print lines after matched line")
    private int linesAfter = 0;

    @Parameters(index = "0")
    private String pattern;

    @Parameters(index = "1..*")
    private List<String> files;

    @Override
    public String execute(String prevResult, Environment environment) throws IOException {
        return null;
    }
}
