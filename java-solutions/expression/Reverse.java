package expression;

public class Reverse extends UnaryOperation {

    public Reverse(AllExpression first) {
        super(first, "reverse", 4);
    }

    @Override
    public int makeAction(int first) {
        int ans = 0;
        while (first != 0) {
            ans *= 10;
            ans += first % 10;
            first /= 10;
        }
        return ans;
    }

    @Override
    public double makeAction(double first) {
        return 0;
    }
}
