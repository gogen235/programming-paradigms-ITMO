package expression.generic;

public class Minus<T extends Number> extends UnaryOperation<T> {
    Minus(AllExpression<T> first, Calculate<T> mode) {
        super(first, "-", 3, mode);
    }

    @Override
    public T makeAction(T first) {
        return getMode().minus(first);
    }
}
