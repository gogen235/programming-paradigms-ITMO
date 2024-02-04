package expression;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Minus(new Minus(new Variable("z"))).toMiniString());
    }
}
