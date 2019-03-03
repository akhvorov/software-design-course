package ru.ifmo.sdc.cli.commands;

import ru.ifmo.sdc.cli.Environment;

/**
 * Assign command. Add variable to environment
 */
public class AssignCommand extends Command {

    @Override
    public String execute(final String prevResult, final Environment environment) {
        final String assignation = commandName;
        if (assignation != null && assignation.contains("=")) {
            final int equalsInd = assignation.indexOf('=');
            final String key = assignation.substring(0, equalsInd);
            final String value = assignation.substring(equalsInd + 1);
            environment.put(key, value);
        }
        return "";
    }
}
