package expression.parser;

public class Main {
    public static void main(String[] args) {
        System.out.println(ExpressionParser.parse(new StringSource("(4 lcm -2147483648)")).evaluate(1));
    }
}
