package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Echo command. Print provided line
 */
public class EchoCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> words = new ArrayList<>();

    @Override
    public String execute(final String prevResult, final Environment environment) {
        return String.join(" ", words);
    }
}
