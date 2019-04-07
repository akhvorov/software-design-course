package ru.ifmo.sdc.cli.commands;

import org.junit.Test;
import ru.ifmo.sdc.cli.Environment;
import ru.ifmo.sdc.cli.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandsTest {
    @Test
    public void testEcho() throws IOException {
        final Command command = CommandFactory.getCommand(Arrays.asList("echo", "abcdef'ghijhk\"lm\"noprstuv"));
        final String result = command.execute("", new Environment());
        assertEquals("abcdef'ghijhk\"lm\"noprstuv", result);
    }

    @Test
    public void testEchoWithSpaces() throws IOException {
        final Command command = CommandFactory.getCommand(Arrays.asList("echo", "abc", "def'g  hi", "jhk\"lm\"noprstuv"));
        final String result = command.execute("", new Environment());
        assertEquals("abc def'g  hi jhk\"lm\"noprstuv", result);
    }

    @Test
    public void testPwd() throws IOException {
        final String pwdCommand = "pwd";
        final Command command = CommandFactory.getCommand(Collections.singletonList(pwdCommand));
        final String result = command.execute("", new Environment());
        assertEquals(Utils.executeExternal(pwdCommand), result);
    }

    @Test
    public void testCat() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        final String commandText = "cat " + tempFileName;
        Utils.writeToTempFile(tempFileName, fileContent);
        final Command command = CommandFactory.getCommand(Collections.singletonList(commandText));
        final String result = command.execute("", new Environment());
        assertEquals(fileContent, result);
    }

    @Test
    public void testCatFromPrevious() throws IOException {
        final String previous = "The first line\nThe second line";
        final Command command = CommandFactory.getCommand(Collections.singletonList("cat"));
        final String result = command.execute(previous, new Environment());
        assertEquals(previous, result);
    }

    @Test
    public void testWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        Utils.writeToTempFile(tempFileName, fileContent);
        final Command command = CommandFactory.getCommand(Arrays.asList("wc", tempFileName));
        final String result = command.execute("", new Environment());
        assertEquals("2 6 31", result);
    }

    @Test
    public void testExit() {
        final Command command = CommandFactory.getCommand(Collections.singletonList("exit"));
        assertTrue(command.isTerminate());
    }

    @Test
    public void testAssign() throws IOException {
        final Command command = CommandFactory.getCommand(Collections.singletonList("x=echo"));
        final Environment environment = new Environment();
        command.execute("", environment);
        assertEquals("echo", environment.get("x"));
    }

    @Test
    public void testGrepSingleLine() throws IOException {
        testGrepFromFile("The first line", "lin", Collections.emptyList());
    }

    @Test
    public void testGrepMultipleLine() throws IOException {
        testGrepFromFile("The first line\nThe Second Line", "lin", Collections.emptyList());
    }

    @Test
    public void testGrepCaseInsensitive() throws IOException {
        testGrepFromFile("The first line\nThe Second Line", "lin", Collections.singletonList("-i"));
    }

    @Test
    public void testGrepWholeWord() throws IOException {
        testGrepFromFile("The first line 1\nThe Second line2", "line", Collections.singletonList("-w"));
    }

    @Test
    public void testGrepWithNextStrings() throws IOException {
        testGrepFromFile("The first line 1\nThe Second line2\nh\ne\nl\nl\no", "lin", Arrays.asList("-A", "2"));
    }

    private void testGrepFromFile(final String content, final String pattern, final List<String> options) throws IOException {
        final String tempFileName = "temp.file";
        Utils.writeToTempFile(tempFileName, content);
        final String commandText = "grep " + String.join(" ", options) + " " + pattern + " " + tempFileName;
        List<String> args = new ArrayList<>();
        args.add("grep");
        args.addAll(options);
        args.addAll(Arrays.asList(pattern, tempFileName));
        final Command command = CommandFactory.getCommand(args);
        final String result = command.execute("", new Environment());
        final String externalResult = Utils.executeExternal(commandText);
        assertEquals(externalResult, result);
    }
}
