package ru.ifmo.sdc.cli;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ShellTest {
    @Test
    public void testCatWc() throws IOException {
        final String tempFileName = "temp.file";
        final String fileContent = "The first line\nThe second line";
        Utils.writeToTempFile(tempFileName, fileContent);
        final String result = Utils.executeCommands(Collections.singletonList("cat " + tempFileName + " | wc"));
        assertEquals("2 6 31", result);
    }

    @Test
    public void testEchoWc() throws IOException {
        final String result = Utils.executeCommands(Collections.singletonList("echo The first line \"  \" | wc"));
        assertEquals("1 3 18", result);
    }

    @Test
    public void testAssignedEcho() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=echo", "$x  sg    sdf\"s    f\"sdf"));
        assertEquals("sg sdfs    fsdf", result);
    }

    @Test
    public void testPartlyAssignedEcho() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=ho", "ec$x  sg    sdf\"s    f\"sdf"));
        assertEquals("sg sdfs    fsdf", result);
    }

    @Test
    public void testVarInStrongQuotes() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=ho", "echo sdf\"s$x   f\"sdf"));
        assertEquals("sdfsho   fsdf", result);
    }

    @Test
    public void testVarInWeakQuotes() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=ho", "echo sdf's$x   f'sdf"));
        assertEquals("sdfs$x   fsdf", result);
    }

    @Test
    public void testVarWithoutQuotes() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=ho", "echo sdfs$x   fsdf"));
        assertEquals("sdfsho fsdf", result);
    }

    @Test
    public void testAssignEchoWc() throws IOException {
        final String result = Utils.executeCommands(Arrays.asList("x=echo", "y=wc", "$x  sg    sdf\"s    f\"sdf |$y"));
        assertEquals("1 3 16", result);
    }

    @Test
    public void testGrepFromEcho() throws IOException {
        final String text = "Zero Line 0\nFirst line 1\nsecond line2\nh\ne\nl\nl line \no\ny";
        final String command = "echo \"" + text + "\" | grep " + " line -i -A 1 -w ";
        final String result = Utils.executeCommands(Collections.singletonList(command));
        assertEquals("Zero Line 0\nFirst line 1\nsecond line2\nl line \no", result);
    }
}