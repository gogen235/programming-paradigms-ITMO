package expression;

public class Divide extends BinaryOperation {
    public Divide(AllExpression first, AllExpression second) {
        super(first, second, "/", 3, false);
    }
    public int makeAction(int first, int second) {
        return first / second;
    }
    public double makeAction(double first, double second) {
        return first / second;
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 13 + super.getSecond().hashCode() * 37;
    }
}
