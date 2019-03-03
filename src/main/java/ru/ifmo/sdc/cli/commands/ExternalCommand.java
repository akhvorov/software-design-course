package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Command for non implemented commands
 */
public class ExternalCommand extends Command {
    @CommandLine.Parameters(index = "1..*")
    private List<String> args = new ArrayList<>();

    @Override
    public String execute(final String prevResult, final Environment environment) throws IOException {
        final Process child;
        try {
            child = Runtime.getRuntime().exec(commandName + String.join(" ", args));
        } catch (IOException e) {
            System.err.println("Can't execute external command");
            throw new IOException(e);
        }
        final BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(child.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String s;
        boolean firstString = true;
        while ((s = stdInput.readLine()) != null) {
            if (!firstString) {
                sb.append('\n');
            }
            sb.append(s);
            firstString = false;
        }
        return sb.toString();
    }
}
