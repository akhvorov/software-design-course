package ru.ifmo.sdc.shell;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShellTest {
    @Test
    public void ShellGet3() {
        assertEquals(3, new Shell().get3());
    }
}
