package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Change working directory to first argument or home.
 */
public class CdCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> params = new ArrayList<>();

    @Override
    public String execute(final String prevResult, final Environment environment) throws IOException {
        final Path relativePath = Paths.get(params.isEmpty() ? environment.get("HOME") : params.get(0));
        final Path path = relativePath.isAbsolute() ?
                relativePath :
                Paths.get(environment.get("PWD"), relativePath.toString()).toAbsolutePath();
        if (!path.toFile().isDirectory()) {
            System.err.println("cd: " + path + ": No such file or directory");
            throw new IOException();
        }
        environment.put(
                "PWD",
                path.toString()
        );
        return "";
    }
}
