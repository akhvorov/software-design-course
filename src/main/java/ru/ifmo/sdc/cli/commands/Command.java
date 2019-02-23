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
        if (tokens.size() > 0 && tokens.get(0).equals(" ")) {
            tokens = tokens.subList(1, tokens.size());
        }
        if (tokens.size() > 0 && tokens.get(tokens.size() - 1).equals(" ")) {
            tokens = tokens.subList(0, tokens.size() - 1);
        }
        tokens.stream().filter(t -> !t.equals("'") && !t.equals("\"")).forEach(sb::append);
        return sb.toString();
    }

    public boolean isTerminate() {
        return false;
    }
}
