package expression.generic;

public class Mod<T extends Number> extends BinaryOperation<T> {

    Mod(AllExpression<T> first, AllExpression<T> second, Calculate<T> mode) {
        super(first, second, "mod", 2, false, mode);
    }

    @Override
    public T makeAction(T first, T second) {
        return getMode().mod(first, second);
    }
}
