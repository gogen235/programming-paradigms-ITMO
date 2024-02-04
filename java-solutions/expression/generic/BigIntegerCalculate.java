package expression.generic;

import java.math.BigInteger;

public class BigIntegerCalculate implements Calculate<BigInteger> {

    @Override
    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger sub(BigInteger first, BigInteger second) {
        return first.subtract(second);
    }

    @Override
    public BigInteger mul(BigInteger first, BigInteger second) {
        return first.multiply(second);
    }

    @Override
    public BigInteger div(BigInteger first, BigInteger second) {
        return first.divide(second);
    }

    @Override
    public BigInteger minus(BigInteger first) {
        return first.negate();
    }

    @Override
    public BigInteger abs(BigInteger first) {
        return first.abs();
    }

    @Override
    public BigInteger square(BigInteger first) {
        return first.multiply(first);
    }

    @Override
    public BigInteger mod(BigInteger first, BigInteger second) {
        return first.mod(second);
    }

    @Override
    public BigInteger parse(String string) {
        return new BigInteger(string);
    }

    @Override
    public BigInteger parseInt(int value) {
        return new BigInteger(String.valueOf(value));
    }
}
