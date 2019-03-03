package ru.ifmo.sdc.cli.commands;

import picocli.CommandLine;
import ru.ifmo.sdc.cli.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Assign command. Add variable to environment
 */
public class AssignCommand extends Command {

    @Override
    public String execute(String prevResult, Environment environment) {
        if (commandName.contains("=")) {
            String assignation = commandName;
            int equalsInd = assignation.indexOf('=');
            String key = assignation.substring(0, equalsInd);
            String value = assignation.substring(equalsInd + 1);
            environment.put(key, value);
        }
        return "";
    }
}
