package expression.generic;

import java.util.Objects;

public class Const<T extends Number> implements AllExpression<T> {
    private final T value;
    public Const(T value) {
        this.value = value;
    }

    public int getPriority() {
        return 3;
    }

    public boolean getCommutative() {
        return true;
    }

    public T evaluate(T x, T y, T z) {
        return value;
    }
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
