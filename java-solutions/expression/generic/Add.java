package expression.generic;

public class Add<T extends Number> extends BinaryOperation<T> {
    Add(AllExpression<T> first, AllExpression<T> second, Calculate<T> mode) {
        super(first, second, "+", 1, true, mode);
    }

    @Override
    public T makeAction(T first, T second) {
        return getMode().add(first, second);
    }
    @Override
    public int hashCode() {
        return super.getFirst().hashCode() * 31 + super.getSecond().hashCode() * 17;
    }
}
