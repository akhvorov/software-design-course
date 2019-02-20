package ru.ifmo.sdc.cli.commands;

import java.io.*;
import java.util.List;

public class ExternalCommand extends Command {
    public ExternalCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) throws IOException {
        Process child = Runtime.getRuntime().exec(args.stream().reduce("", (x, y) -> x + y));
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
