package expression;

import java.util.Objects;

public class Const implements AllExpression{
    private final Number value;
    protected final boolean isInt;

    public Const(int value) {
        this.value = value;
        this.isInt = true;
    }
    public Const(double value) {
        this.value = value;
        this.isInt = false;
    }

    public Number getValue() {
        return value;
    }
    public int getPriority() {
        return 4;
    }
    public boolean getCommutative() {
        return true;
    }

    @Override
    public int evaluate(int var) {
        return value.intValue();
    }
    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
    @Override
    public double evaluate(double x) {
        return value.doubleValue();
    }
    @Override
    public String toString() {
        if (isInt) {
            return Integer.toString(value.intValue());
        }
        return Double.toString(value.doubleValue());
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    public boolean equals(Object elem) {
        if (elem instanceof Const newElem) {
            return Objects.equals(newElem.getValue(), value);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
