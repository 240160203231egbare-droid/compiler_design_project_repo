import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Parser {
    private final List<Production> productions;

    public Parser(List<Production> productions) {
        this.productions = new ArrayList<>(productions);

        // Prefer longer right-hand sides when more than one reduction matches.
        this.productions.sort(
            Comparator.comparingInt((Production p) -> p.getRight().length()).reversed()
        );
    }

    public boolean parse(String input) {
        StringBuilder stack = new StringBuilder("$");
        int inputIndex = 0;
        int step = 1;
        int maxSteps = Math.max(100, input.length() * 20);

        printHeader();

        while (step <= maxSteps) {
            Production matchingProduction = findMatchingProduction(stack);

            if (matchingProduction != null) {
                String right = matchingProduction.getRight();
                int start = stack.length() - right.length();

                stack.replace(start, stack.length(), matchingProduction.getLeft());

                printRow(step++, stack.toString(),
                         input.substring(inputIndex) + "$",
                         "REDUCE " + matchingProduction);
                continue;
            }

            if (inputIndex < input.length()) {
                char nextSymbol = input.charAt(inputIndex);
                stack.append(nextSymbol);
                inputIndex++;

                printRow(step++, stack.toString(),
                         input.substring(inputIndex) + "$",
                         "SHIFT " + nextSymbol);
            } else {
                break;
            }
        }

        boolean accepted = stack.toString().equals("$S")
                           && inputIndex == input.length();

        if (accepted) {
            printRow(step, stack.toString(), "$", "ACCEPT");
            System.out.println("\nRESULT: STRING ACCEPTED");
        } else {
            printRow(step, stack.toString(), "$", "ERROR");
            System.out.println("\nRESULT: STRING REJECTED");
        }

        return accepted;
    }

    private Production findMatchingProduction(StringBuilder stack) {
        for (Production production : productions) {
            String right = production.getRight();

            if (stack.length() >= right.length()
                    && stack.substring(stack.length() - right.length())
                             .equals(right)) {
                return production;
            }
        }
        return null;
    }

    private void printHeader() {
        System.out.printf("%-8s %-18s %-18s %-25s%n",
                          "STEP", "STACK", "INPUT", "ACTION");
        System.out.println("--------------------------------------------------------------------------");
    }

    private void printRow(int step, String stack, String remainingInput,
                          String action) {
        System.out.printf("%-8d %-18s %-18s %-25s%n",
                          step, stack, remainingInput, action);
    }
}

