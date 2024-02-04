package expression;

public class Pow10 extends UnaryOperation {
    public Pow10(AllExpression first) {
        super(first, "pow10", 4);
    }

    @Override
    public int makeAction(int first) {
        int result = 1;
        for (int i = 0; i < first; i++) {
            result *= 10;
        }
        return result;
    }

    @Override
    public double makeAction(double first) {
        return 0;
    }
}