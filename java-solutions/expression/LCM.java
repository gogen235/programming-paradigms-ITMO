package expression;

public class LCM extends BinaryOperation {
    public LCM(AllExpression first, AllExpression second) {
        super(first, second, "lcm", 1, true);
    }

    @Override
    public int makeAction(int first, int second) {
        if (gcd(abs(first), abs(second)) == 0) {
            return 0;
        }
        return first / abs(gcd(first, second)) * second;
    }

    @Override
    public double makeAction(double first, double second) {
        return 0;
    }
    protected int gcd(int first, int second) {
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
