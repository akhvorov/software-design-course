package ru.ifmo.sdc.cli;

import ru.ifmo.sdc.cli.commands.Command;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for shell execution
 */
public class Shell {
    /**
     * Entry point to program
     *
     * @param args arguments of program will be ignored
     */
    public static void main(String[] args) throws IOException {
        new Shell().run();
    }

    /**
     * Main loop of shell for read commands and write result
     */
    private void run() {
        final Scanner scanner = new Scanner(System.in);
        final Environment environment = new Environment(System.getenv());
        boolean isAlive = true;
        while (isAlive) {
            System.out.print("$ ");
            final String line = scanner.nextLine();
            final List<Command> commands = Parser.parse(line, environment);
            String prevResult = "";
            for (Command command : commands) {
                if (command.isTerminate()) {
                    isAlive = false;
                    break;
                }
                try {
                    prevResult = command.execute(prevResult, environment);
                } catch (IOException e) {
                    System.err.println("Execution interrupted");
                    break;
                }
            }
            if (!prevResult.isEmpty()) {
                System.out.println(prevResult);
            }
        }
    }
}
