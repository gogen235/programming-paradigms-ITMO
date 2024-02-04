package expression.generic;

public class Divide<T extends Number> extends BinaryOperation<T> {
    Divide(AllExpression<T> first, AllExpression<T> second, Calculate<T> mode) {
        super(first, second, "/", 2, false, mode);
    }

    @Override
    public T makeAction(T first, T second) {
        return getMode().div(first, second);
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 13 + super.getSecond().hashCode() * 37;
    }
}
