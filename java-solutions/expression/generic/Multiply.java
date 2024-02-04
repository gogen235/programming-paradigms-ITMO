package expression.generic;

public class Multiply<T extends Number> extends BinaryOperation<T> {
    Multiply(AllExpression<T> first, AllExpression<T> second, Calculate<T> mode) {
        super(first, second, "*", 2, true, mode);
    }

    @Override
    public T makeAction(T first, T second) {
        return getMode().mul(first, second);
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 41 + super.getSecond().hashCode() * 101;
    }
}
