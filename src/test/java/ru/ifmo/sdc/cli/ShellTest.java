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
    public void testCdWithoutArgs() throws IOException {
        final String result = executeCommands(Collections.singletonList("cd"), Collections.singletonList("pwd"));
        assertEquals(System.getenv().get("HOME") + '\n', result);
    }

    @Test
    public void testCdWithArgs() throws IOException {
        final String result = executeCommands(Collections.singletonList("cd src"), Collections.singletonList("pwd"));
        assertEquals(Paths.get(System.getProperty("user.dir"), "src").toString() + '\n', result);
    }

    @Test
    public void testLsWithoutArgs() throws IOException {
        final String result = executeCommands(Collections.singletonList("ls"));
        assertTrue(result.contains("README.md\n"));
    }

    @Test
    public void testLsWithArgs() throws IOException {
        final String result = executeCommands(Collections.singletonList("ls src"));
        assertTrue(result.contains("test\n"));
        assertTrue(result.contains("main\n"));
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
        assertEquals(executeExternal(pwdCommand) + '\n', result);
    }

    @Test
    public void testCat() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        writeToTempFile(tempFileName, fileContent);
        final String result = executeCommands(Collections.singletonList("cat " + tempFileName));
        final String externalResult = executeExternal("cat " + tempFileName);
        assertEquals(externalResult, result);
        assertEquals(fileContent, result);
    }

    @Test
    public void testWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        writeToTempFile(tempFileName, fileContent);
        final String result = executeCommands(Collections.singletonList("wc " + tempFileName));
        assertEquals("2 6 31", result);
    }

    @Test
    public void testCatWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        writeToTempFile(tempFileName, fileContent);
        final String result = executeCommands(Collections.singletonList("cat " + tempFileName + " | wc"));
        assertEquals("2 6 31", result);
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

    @Test
    public void testGrepSingleLine() throws IOException {
        testGrepFromFile("The first line", "lin", "");
    }

    @Test
    public void testGrepMultipleLine() throws IOException {
        testGrepFromFile("The first line\nThe Second Line", "lin", "");
    }

    @Test
    public void testGrepCaseInsensitive() throws IOException {
        testGrepFromFile("The first line\nThe Second Line", "lin", "-i");
    }

    @Test
    public void testGrepWholeWord() throws IOException {
        testGrepFromFile("The first line 1\nThe Second line2", "line", "-w");
    }

    @Test
    public void testGrepWithNextStrings() throws IOException {
        testGrepFromFile("The first line 1\nThe Second line2\nh\ne\nl\nl\no", "lin", "-A 2");
    }

    private void testGrepFromFile(final String content, final String pattern, final String options) throws IOException {
        final String tempFileName = "temp.file";
        writeToTempFile(tempFileName, content);
        final String command = "grep " + options + " " + pattern + " " + tempFileName;
        final String result = executeCommands(Collections.singletonList(command));
        final String externalResult = executeExternal(command);
        assertEquals(externalResult, result);
    }

    @Test
    public void testGrepFromEcho() throws IOException {
        final String text = "Zero Line 0\nFirst line 1\nsecond line2\nh\ne\nl\nl line \no\ny";
        final String command = "echo \"" + text + "\" | grep " + " line -i -A 1 -w ";
        final String result = executeCommands(Collections.singletonList(command));
        assertEquals("Zero Line 0\nFirst line 1\nsecond line2\nl line \no", result);
    }

    private String executeCommands(final List<String>... pipelinedLines) throws IOException {
        final Environment environment = new Environment(System.getenv());
        String prevResult = "";
        for (List<String> pipelinedLine : pipelinedLines) {
            for (String pipelineCommand : pipelinedLine) {
                final List<Command> commands = Parser.parse(pipelineCommand, environment);
                for (Command command : commands) {
                    prevResult = command.execute(prevResult, environment);
                }
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

    private void writeToTempFile(final String name, final String content) throws IOException {
        final Path file = Paths.get(name);
        Files.write(file, Collections.singleton(content));
        new File(file.toString()).deleteOnExit();
    }
}
