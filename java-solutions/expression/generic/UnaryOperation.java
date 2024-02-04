package expression.generic;

public abstract class UnaryOperation<T extends Number> implements AllExpression<T> {
    private final AllExpression<T> first;
    String operator;
    private final int priority;
    private final Calculate<T> mode;
    UnaryOperation(AllExpression<T> first, String operator, int priority, Calculate<T> mode) {
        this.first = first;
        this.operator = operator;
        this.priority = priority;
        this.mode = mode;
    }
    public AllExpression<T> getFirst() {
        return first;
    }

    @Override
    public int getPriority() {
        return priority;
    }
    public boolean getCommutative() {
        return true;
    }
    public Calculate<T> getMode() { return mode; }

    public abstract T makeAction(T first);

    @Override
    public T evaluate(T x, T y, T z) {
        return makeAction(first.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return  operator + "(" + first.toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (first.getPriority() < priority) {
            return operator + "(" + first.toMiniString() + ")";
        }
        return operator + " " + first.toMiniString();
    }
}
