package expression.generic;

public class Square<T extends Number> extends UnaryOperation<T> {
    Square(AllExpression<T> first, Calculate<T> mode) {
        super(first, "square", 3, mode);
    }

    @Override
    public T makeAction(T first) {
        return getMode().square(first);
    }
}
