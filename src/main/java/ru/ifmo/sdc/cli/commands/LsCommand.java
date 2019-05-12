package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * List directory contents. If args are empty, then list working directory contents.
 */
public class LsCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> params = new ArrayList<>();

    @Override
    public String execute(final String prevResult, final Environment environment) throws IOException {
        StringJoiner stringBuilder = new StringJoiner("\n");
        if (params.isEmpty()) {
            list(environment, stringBuilder, "");
        } else {
            for (String path : params) {
                if (params.size() > 1)
                    stringBuilder.add(path);
                list(environment, stringBuilder, path);
            }
        }
        return stringBuilder.toString();
    }

    private void list(Environment environment, StringJoiner stringBuilder, String path) throws IOException {
        final File file = Paths.get(environment.userDir, path).toFile();
        if (file.exists())
            if (file.isDirectory())
                for (String subpath : file.list())
                    stringBuilder.add(subpath);
            else
                stringBuilder.add(path);
        else {
            System.err.println("ls: " + path + ": No such file or directory");
            throw new IOException();
        }
    }
}
