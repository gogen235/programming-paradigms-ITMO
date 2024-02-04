package expression;

public class Subtract extends BinaryOperation{
    public Subtract(AllExpression first, AllExpression second) {
        super(first, second, "-", 2, false);
    }
    public int makeAction(int first, int second) {
        return first - second;
    }
    public double makeAction(double first, double second) {
        return first - second;
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 11 + super.getSecond().hashCode() * 43;
    }
}
