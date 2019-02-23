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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ShellTest {
    @Test
    public void testEcho() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "echo abc\"def'gh\"ij'hk\"lm\"no'prstuv";
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        assertEquals("abcdef'ghijhk\"lm\"noprstuv", prevResult);
    }

    @Test
    public void testPwd() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "pwd";
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        assertEquals(executeExternal(line), prevResult);
    }

    @Test
    public void testCat() throws IOException {
        String tempFileName = "temp.file";
        String fileContent = "The first line\nThe second line";
        Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "cat " + tempFileName;
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        new File(file.toString()).deleteOnExit();
        assertEquals(fileContent, prevResult);
    }

    @Test
    public void testWc() throws IOException {
        String tempFileName = "temp.file";
        String fileContent = "The first line\nThe second line";
        Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "wc " + tempFileName;
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        new File(file.toString()).deleteOnExit();
        assertEquals("2 6 31", prevResult);
    }

    @Test
    public void testCatWc() throws IOException {
        String tempFileName = "temp.file";
        String fileContent = "The first line\nThe second line";
        Path file = Paths.get(tempFileName);
        Files.write(file, Collections.singleton(fileContent));

        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "cat " + tempFileName + " | wc";
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        new File(file.toString()).deleteOnExit();
        assertEquals("2 6 30", prevResult);
    }

    @Test
    public void testEchoWc() throws IOException {
        String content = "The first line\nThe second line";
        Environment environment = new Environment();
        Parser parser = new Parser();
        String line = "echo " + content + " | wc";
        List<Command> commands = parser.parse(line, environment);
        String prevResult = "";
        for (Command command : commands) {
            prevResult = command.execute(prevResult);
        }
        assertEquals("2 6 30", prevResult);
    }

    private String executeExternal(String command) throws IOException {
        Process child = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(child.getInputStream()));
        StringBuilder sb = new StringBuilder();
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