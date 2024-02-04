package expression.generic;

public class Abs<T extends Number> extends UnaryOperation<T> {
    Abs(AllExpression<T> first, Calculate<T> mode) {
        super(first, "abs", 3, mode);
    }

    @Override
    public T makeAction(T first) {
        return getMode().abs(first);
    }
}
