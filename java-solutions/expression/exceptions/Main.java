package expression.exceptions;

import expression.parser.StringSource;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String line = in.nextLine();
            System.out.println(ExpressionParser.parse(new StringSource(line)).toMiniString());
        }
    }
}
