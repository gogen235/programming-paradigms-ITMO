package expression;

public class GCD extends BinaryOperation {
    public GCD(AllExpression first, AllExpression second) {
        super(first, second, "gcd", 1, true);
    }

    @Override
    public int makeAction(int first, int second) {
        return abs(gcd(first, second));
    }

    @Override
    public double makeAction(double first, double second) {
        return 0;
    }
    private int gcd(int first, int second) {
        if (second == 0) {
            return first;
        }
        return gcd(second, first % second);
    }
    private int abs(int num) {
        if (num < 0) {
            return -num;
        }
        return num;
    }
}
