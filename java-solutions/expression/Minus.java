package expression;

public class Minus extends UnaryOperation {
    public Minus(AllExpression first) {
        super(first, "-", 4);
    }
    @Override
    public int makeAction(int first) {
        return -first;
    }

    @Override
    public double makeAction(double first) {
        return -first;
    }
}
