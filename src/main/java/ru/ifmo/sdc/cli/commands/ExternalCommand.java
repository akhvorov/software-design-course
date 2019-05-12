package ru.ifmo.sdc.cli.commands;

import ru.ifmo.sdc.cli.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Command for non implemented commands
 */
public class ExternalCommand extends Command {
    private List<String> args;

    public ExternalCommand(String[] args) {
        this.args = Arrays.asList(args).subList(1, args.length);
    }

    @Override
    public String execute(final String prevResult, final Environment environment) throws IOException {
        final Process child;
        try {
            child = Runtime.getRuntime().exec(commandName + " " + String.join(" ", args));
        } catch (IOException e) {
            System.err.println("Can't execute external command");
            throw new IOException(e);
        }
        final StringBuilder sb = new StringBuilder();
        readStream(child.getInputStream(), sb);
        readStream(child.getErrorStream(), sb);
        return sb.toString();
    }

    private void readStream(final InputStream stream, final StringBuilder sb) throws IOException {
        final BufferedReader stdInput = new BufferedReader(new InputStreamReader(stream));
        String s;
        boolean firstString = true;
        while ((s = stdInput.readLine()) != null) {
            if (!firstString) {
                sb.append('\n');
            }
            sb.append(s);
            firstString = false;
        }
    }
}
