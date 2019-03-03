package ru.ifmo.sdc.cli;

import org.junit.Test;
import ru.ifmo.sdc.cli.commands.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ShellTest {
    @Test
    public void testEcho() throws IOException {
        final String result = executeCommands(Collections.singletonList("echo abc\"def'gh\"ij'hk\"lm\"no'prstuv"));
        assertEquals("abcdef'ghijhk\"lm\"noprstuv", result);
    }

    @Test
    public void testEchoWithSpaces() throws IOException {
        final String result = executeCommands(Collections.singletonList("echo   abc  \"def'g  h\"i   j'hk\"lm\"no'prstuv  "));
        assertEquals("abc def'g  hi jhk\"lm\"noprstuv", result);
    }

    @Test
    public void testPwd() throws IOException {
        final String pwdCommand = "pwd";
        final String result = executeCommands(Collections.singletonList(pwdCommand));
        assertEquals(executeExternal(pwdCommand), result);
    }

    @Test
    public void testCat() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        final Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        new File(file.toString()).deleteOnExit();
        final String result = executeCommands(Collections.singletonList("cat " + tempFileName));
        final String externalResult = executeExternal("cat " + tempFileName);
        assertEquals(externalResult, result);
        assertEquals(fileContent, result);
    }

    @Test
    public void testWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        final Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        final String result = executeCommands(Collections.singletonList("wc " + tempFileName));
        assertEquals("2 6 31", result);
        new File(file.toString()).deleteOnExit();
    }

    @Test
    public void testCatWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        final Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        final String result = executeCommands(Collections.singletonList("cat " + tempFileName + " | wc"));
        assertEquals("2 6 31", result);
        new File(file.toString()).deleteOnExit();
    }

    @Test
    public void testEchoWc() throws IOException {
        final String result = executeCommands(Collections.singletonList("echo The first line \"  \" | wc"));
        assertEquals("1 3 18", result);
    }

    @Test
    public void testAssignedEcho() throws IOException {
        final String result = executeCommands(Arrays.asList("x=echo", "$x  sg    sdf\"s    f\"sdf"));
        assertEquals("sg sdfs    fsdf", result);
    }

    @Test
    public void testPartlyAssignedEcho() throws IOException {
        final String result = executeCommands(Arrays.asList("x=ho", "ec$x  sg    sdf\"s    f\"sdf"));
        assertEquals("sg sdfs    fsdf", result);
    }

    @Test
    public void testVarInStrongQuotes() throws IOException {
        final String result = executeCommands(Arrays.asList("x=ho", "echo sdf\"s$x   f\"sdf"));
        assertEquals("sdfsho   fsdf", result);
    }

    @Test
    public void testVarInWeakQuotes() throws IOException {
        final String result = executeCommands(Arrays.asList("x=ho", "echo sdf's$x   f'sdf"));
        assertEquals("sdfs$x   fsdf", result);
    }

    @Test
    public void testVarWithoutQuotes() throws IOException {
        final String result = executeCommands(Arrays.asList("x=ho", "echo sdfs$x   fsdf"));
        assertEquals("sdfsho fsdf", result);
    }

    @Test
    public void testAssignEchoWc() throws IOException {
        final String result = executeCommands(Arrays.asList("x=echo", "y=wc", "$x  sg    sdf\"s    f\"sdf |$y"));
        assertEquals("1 3 16", result);
    }

    private String executeCommands(final List<String> lines) throws IOException {
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

    private String executeExternal(final String command) throws IOException {
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
}