package expression.generic;

public class Subtract<T extends Number> extends BinaryOperation<T> {
    Subtract(AllExpression<T> first, AllExpression<T> second, Calculate<T> mode) {
        super(first, second, "-", 1, false, mode);
    }

    @Override
    public T makeAction(T first, T second) {
        return getMode().sub(first, second);
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 11 + super.getSecond().hashCode() * 43;
    }
}
