package ru.ifmo.sdc.cli.commands;

import java.io.IOException;
import java.util.List;

public abstract class Command {
    protected final List<String> args;

    public Command(List<String> args) {
        this.args = args;
    }

    public abstract String execute(String prevResult) throws IOException;

    protected String tokensToString(List<String> tokens) {
        StringBuilder sb = new StringBuilder();
        tokens.stream().filter(t -> !t.equals("'") && !t.equals("\"")).forEach(sb::append);
        return sb.toString().trim();
    }
}
