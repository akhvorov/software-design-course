package ru.ifmo.sdc.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Tokenize string on tokens for command line parameters and options convenient parsing
 */
public class Tokenizer {
    private boolean lastTokenIsQuoted = false;
    private final StringBuilder current = new StringBuilder();
    private final List<String> result = new ArrayList<>();
    private QuotesState state = QuotesState.ORDINARY;

    /**
     * Create tokenizer object
     *
     * @param string String to tokenize
     */
    public Tokenizer(final String string) {
        if (string == null || string.length() == 0) {
            return;
        }
        final StringTokenizer tok = new StringTokenizer(string, "\"\' |", true);
        while (tok.hasMoreTokens()) {
            final String token = tok.nextToken();
            switch (state) {
                case WEAK:
                    acceptThisTypeOfQuotesOrAdd(token, "\'");
                    break;
                case STRONG:
                    acceptThisTypeOfQuotesOrAdd(token, "\"");
                    break;
                default:
                    acceptOrdinary(token);
                    break;
            }
        }
        if (lastTokenIsQuoted || current.length() != 0) {
            result.add(current.toString());
        }
    }

    /**
     * Return parsed tokens
     *
     * @return Parsed tokens
     */
    public List<String> tokens() {
        return result;
    }

    /**
     * Update state in quotes
     *
     * @param token Current token
     * @param quotes Quotes string of this current quotes type
     */
    private void acceptThisTypeOfQuotesOrAdd(final String token, final String quotes) {
        if (token.equals(quotes)) {
            lastTokenIsQuoted = true;
            state = QuotesState.ORDINARY;
        } else {
            current.append(token);
        }
    }

    /**
     * Accept token out of quotes
     *
     * @param token Current token
     */
    private void acceptOrdinary(final String token) {
        switch (token) {
            case "\'":
                state = QuotesState.WEAK;
                break;
            case "\"":
                state = QuotesState.STRONG;
                break;
            case " ":
                if (lastTokenIsQuoted || current.length() != 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                break;
            case "|":
                if (current.length() != 0) {
                    result.add(current.toString());
                    current.setLength(0);
                }
                result.add("|");
                break;
            default:
                current.append(token);
                break;
        }
        lastTokenIsQuoted = false;
    }

    /**
     * State of quotes in string
     */
    private enum QuotesState {
        ORDINARY,
        WEAK,
        STRONG
    }
}
