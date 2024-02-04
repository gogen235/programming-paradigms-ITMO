package expression.generic;

public interface Calculate<T extends Number> {
    T add(T first, T second);
    T sub(T first, T second);
    T mul(T first, T second);
    T div(T first, T second);
    T minus(T first);
    T abs(T first);
    T square(T first);
    T mod(T first, T second);
    T parse(String string);
    T parseInt(int value);
}
