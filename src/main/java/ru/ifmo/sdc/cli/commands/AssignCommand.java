package ru.ifmo.sdc.cli.commands;

import java.util.List;

/**
 * Assign command. Do nothing
 */
public class AssignCommand extends Command {
    public AssignCommand(List<String> args) {
        super(args);
    }

    @Override
    public String execute(String prevResult) {
        return "";
    }
}
