package expression.generic;

public interface AllExpression<T extends Number> {
    int getPriority();
    boolean getCommutative();
    T evaluate(T x, T y, T z);
    String toString();
    String toMiniString();
    boolean equals(Object elem);
    int hashCode();
}
