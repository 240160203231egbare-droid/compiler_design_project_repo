import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParserMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Grammar:
        // S -> aSb
        // S -> ab
        List<Production> grammar = new ArrayList<>();
        grammar.add(new Production("S", "aSb"));
        grammar.add(new Production("S", "ab"));

        System.out.println("======================================");
        System.out.println("       BASIC SHIFT-REDUCE PARSER");
        System.out.println("======================================");
        System.out.println("Grammar:");
        System.out.println("1. S -> aSb");
        System.out.println("2. S -> ab");
        System.out.println();

        System.out.print("Enter input string (a's followed by b's): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
            scanner.close();
            return;
        }

        if (!input.matches("[ab]+")) {
            System.out.println("Invalid symbols. Only 'a' and 'b' are allowed.");
            scanner.close();
            return;
        }

        Parser parser = new Parser(grammar);
        parser.parse(input);

        scanner.close();
    }
}

