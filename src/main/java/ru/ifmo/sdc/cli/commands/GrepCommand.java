package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import ru.ifmo.sdc.cli.Environment;

/**
 * Grep command. Find lines that matches regular expression
 */
public class GrepCommand extends Command {
    @Option(names = {"-i"}, description = "Insensitive to register")
    private boolean caseInsensitive = false;

    @Option(names = {"-w"}, description = "Find whole word")
    private boolean wholeWord = false;

    @Option(names = {"-A"}, description = "print lines after matched line")
    private int linesAfter = 0;

    @Parameters(index = "1")
    private String pattern;

    @Parameters(index = "2..*")
    private List<String> files;

    @Override
    public String execute(final String prevResult, final Environment environment) throws IOException {
        final String regex = makeRegex();
        final Pattern pattern = makePattern(regex);
        final List<String> lines = getLines(prevResult);
        final List<String> matched = new ArrayList<>(lines.size());
        int printUntil = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (pattern.matcher(line).matches()) {
                printUntil = i + linesAfter;
            }
            if (i <= printUntil) {
                matched.add(line);
            }
        }
        return String.join("\n", matched);
    }

    /**
     * Create regex from provided pattern and options
     *
     * @return right regex
     */
    private String makeRegex() {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern is not provided");
        }
        if (wholeWord) {
            return ".*\\b" + pattern + "\\b.*";
        }
        return ".*" + pattern + ".*";
    }

    /**
     * Compile the pattern with respect to options
     *
     * @param regex regex
     * @return compiled pattern
     */
    private Pattern makePattern(final String regex) {
        if (caseInsensitive) {
            return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(regex);
    }

    /**
     * Get list of lines for search
     * @param prevResult result of previous command
     * @return list of lines
     * @throws IOException can't read file
     */
    private List<String> getLines(final String prevResult) throws IOException {
        if (files == null || files.isEmpty()) {
            return Arrays.asList(prevResult.split("\n"));
        }
        return Files.readAllLines(Paths.get(files.get(0)));
    }
}
