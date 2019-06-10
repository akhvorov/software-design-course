package ru.ifmo.sdc.cli;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void testEmptySubstitute() {
        Environment environment = new Environment();
        assertEquals(" 10", Parser.substitute(environment, "$x$y 10"));
    }

    @Test
    public void testNothingSubstitute() {
        Environment environment = new Environment();
        assertEquals("echo 10", Parser.substitute(environment, "echo 10"));
    }

    @Test
    public void testSubstitute() {
        Environment environment = new Environment();
        environment.put("x", "ec");
        environment.put("y", "ho");
        assertEquals("echo 10", Parser.substitute(environment, "$x$y 10"));
    }

    @Test
    public void testTokenize() {
        final String string = "echo   sg    sdf\"s    f\"sdf |wc";
        final List<String> expected = Arrays.asList("echo", "sg", "sdfs    fsdf", "|", "wc");
        assertEquals(expected, new Tokenizer(string).tokens());
    }

    @Test
    public void testTokenize2() {
        final String string = "echo sg    sdf\"s    f\"sdf |wc";
        final List<String> expected = Arrays.asList("echo", "sg", "sdfs    fsdf", "|", "wc");
        assertEquals(expected, new Tokenizer(string).tokens());
    }

    @Test
    public void testTokenizeDifferentQuotes() {
        final String string = "echo   abc \"def'g  hi\" jh'k\"lm\"no'prstuv";
        final List<String> expected = Arrays.asList("echo", "abc", "def'g  hi", "jhk\"lm\"noprstuv");
        assertEquals(expected, new Tokenizer(string).tokens());
    }
}
