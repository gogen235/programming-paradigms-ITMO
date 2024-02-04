package expression;

public class Add extends BinaryOperation {
    public Add(AllExpression first, AllExpression second) {
        super(first, second, "+", 2, true);
    }

    public int makeAction(int first, int second) {
        return first + second;
    }

    @Override
    public double makeAction(double first, double second) {
        return first + second;
    }

    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 31 + super.getSecond().hashCode() * 17;
    }
}
