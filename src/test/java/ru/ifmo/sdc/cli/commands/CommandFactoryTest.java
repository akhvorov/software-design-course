package ru.ifmo.sdc.cli.commands;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class CommandFactoryTest {
    @Test
    public void testCommandsClasses() {
        assertTrue(CommandFactory.getCommand(Collections.singletonList("x=echo")) instanceof AssignCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("cat", "temp.file")) instanceof CatCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("cat", "temp.file")) instanceof CatCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("echo", "hello world")) instanceof EchoCommand);
        assertTrue(CommandFactory.getCommand(Collections.singletonList("exit")) instanceof ExitCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("man", "git")) instanceof ExternalCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("grep", "hello", "temp.file")) instanceof GrepCommand);
        assertTrue(CommandFactory.getCommand(Collections.singletonList("pwd")) instanceof PwdCommand);
        assertTrue(CommandFactory.getCommand(Arrays.asList("wc", "temp.file")) instanceof WcCommand);
    }
}
