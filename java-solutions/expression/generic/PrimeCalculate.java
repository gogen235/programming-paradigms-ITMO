package expression.generic;

import expression.exceptions.DivisionByZeroException;

public class PrimeCalculate implements Calculate<Integer> {
    private final int MODULE = 10079;
    @Override
    public Integer add(Integer first, Integer second) {
        return getMode(first + second);
    }

    @Override
    public Integer sub(Integer first, Integer second) {
        return getMode(first - second);
    }

    @Override
    public Integer mul(Integer first, Integer second) {
        return getMode(first * second);
    }

    @Override
    public Integer div(Integer first, Integer second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by zero");
        }
        return getMode(first % MODULE * pow(second % MODULE, MODULE - 2));
    }

    @Override
    public Integer minus(Integer first) {
        return getMode(-first);
    }

    @Override
    public Integer abs(Integer first) {
        return getMode(first);
    }

    @Override
    public Integer square(Integer first) {
        return mul(first, first);
    }

    @Override
    public Integer mod(Integer first, Integer second) {
        return getMode(first % second);
    }

    @Override
    public Integer parse(String string) {
        return getMode(Integer.parseInt(string));
    }

    @Override
    public Integer parseInt(int value) {
        return getMode(value);
    }
    private Integer getMode(Integer value) {
        return ((value % MODULE) + MODULE) % MODULE;
    }
    private Integer pow(Integer value, Integer pow) {
        if (pow == 0) {
            return 1;
        }
        if (pow % 2 == 0) {
            return pow(value * value % MODULE, pow / 2);
        }
        return pow(value, pow - 1) * value % MODULE;
    }
}
