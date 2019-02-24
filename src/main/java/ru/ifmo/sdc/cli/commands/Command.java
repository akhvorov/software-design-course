package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.util.List;

/**
 * Parent class for commands
 */
public abstract class Command {
    protected final List<String> args;

    /**
     * Constructor of command
     *
     * @param args list of tokens
     */
    public Command(List<String> args) {
        this.args = args;
    }

    /**
     * Execute command
     *
     * @param prevResult input from pipe
     * @return result of execution
     * @throws IOException exception to read file or write result of external command execution
     */
    public abstract String execute(String prevResult) throws IOException;

    /**
     * Restore string from tokens
     *
     * @param tokens list of tokens
     * @return restored string
     */
    protected String tokensToString(List<String> tokens) {
        StringBuilder sb = new StringBuilder();
        if (tokens.size() > 0 && tokens.get(0).equals(" ")) {
            tokens = tokens.subList(1, tokens.size());
        }
        if (tokens.size() > 0 && tokens.get(tokens.size() - 1).equals(" ")) {
            tokens = tokens.subList(0, tokens.size() - 1);
        }
        tokens.stream().filter(t -> !t.equals("'") && !t.equals("\"")).forEach(sb::append);
        return sb.toString();
    }

    /**
     * Flag for exit command
     *
     * @return is this command is exit
     */
    public boolean isTerminate() {
        return false;
    }
}
