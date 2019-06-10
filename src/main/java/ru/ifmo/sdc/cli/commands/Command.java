package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine.Parameters;
import ru.ifmo.sdc.cli.Environment;

import java.io.IOException;

/**
 * Parent class for commands
 */
public abstract class Command {
    @Parameters(index = "0")
    protected String commandName = null;

   /**
    * Execute command
    *
    * @param prevResult input from pipe
    * @param environment environment
    * @return result of execution
    * @throws IOException exception to read file or write result of external command execution
    */
    public abstract String execute(final String prevResult, final Environment environment) throws IOException;

    /**
     * Flag for exit command
     *
     * @return is this command is exited
     */
    public boolean isTerminate() {
        return false;
    }
}
