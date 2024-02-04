package expression.generic;

public class ShortCalculate implements Calculate<Short> {
    @Override
    public Short add(Short first, Short second) {
        return (short) (first + second);
    }

    @Override
    public Short sub(Short first, Short second) {
        return (short) (first - second);
    }

    @Override
    public Short mul(Short first, Short second) {
        return (short) (first * second);
    }

    @Override
    public Short div(Short first, Short second) {
        return (short) (first / second);
    }

    @Override
    public Short minus(Short first) {
        return (short) -first;
    }

    @Override
    public Short abs(Short first) {
        return (short) Math.abs(first);
    }

    @Override
    public Short square(Short first) {
        return (short) (first * first);
    }

    @Override
    public Short mod(Short first, Short second) {
        return (short) (first % second);
    }

    @Override
    public Short parse(String string) {
        return Short.parseShort(string);
    }

    @Override
    public Short parseInt(int value) {
        return (short) value;
    }
}
