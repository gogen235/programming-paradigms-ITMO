package expression;

public class Multiply extends BinaryOperation {
    public Multiply(AllExpression first, AllExpression second) {
        super(first, second, "*", 3, true);
    }
    public int makeAction(int first, int second) {
        return first * second;
    }
    public double makeAction(double first, double second) {
        return first * second;
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 41 + super.getSecond().hashCode() * 101;
    }

}
