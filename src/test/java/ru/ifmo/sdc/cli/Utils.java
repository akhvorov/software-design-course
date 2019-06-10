package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static String executeCommands(final List<String> lines) throws IOException {
        final Environment environment = new Environment();
        String prevResult = "";
        for (String line : lines) {
            final List<Command> commands = Parser.parse(line, environment);
            for (Command command : commands) {
                prevResult = command.execute(prevResult, environment);
            }
        }
        return prevResult;
    }

    public static String executeExternal(final String command) throws IOException {
        final Process child = Runtime.getRuntime().exec(command);
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

    public static void writeToTempFile(final String name, final String content) throws IOException {
        final Path file = Paths.get(name);
        Files.write(file, Collections.singleton(content));
        new File(file.toString()).deleteOnExit();
    }
}
