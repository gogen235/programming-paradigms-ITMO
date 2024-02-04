package expression.generic;

import expression.exceptions.CheckedOperations;
import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class CheckedIntegerCalculate implements Calculate<Integer> {

    @Override
    public Integer add(Integer first, Integer second) {
        int result = first + second;
        if (((first ^ result) & (second ^ result)) < 0) {
            throw new OverflowException("overflow in " + first + " + " + second);
        }
        return result;
    }

    @Override
    public Integer sub(Integer first, Integer second) {
        if (second > 0 && first < Integer.MIN_VALUE + second ||
                second < 0 && first > Integer.MAX_VALUE + second) {
            throw new OverflowException("overflow in " + first + " - " + second);
        }
        return first - second;
    }

    @Override
    public Integer mul(Integer first, Integer second) {
        String massage = "overflow in " + first + " * " + second;
        return CheckedOperations.mul(first, second, massage);
    }

    @Override
    public Integer div(Integer first, Integer second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by null");
        } else if (first == Integer.MIN_VALUE && second == -1) {
            throw new OverflowException("overflow in " + first + " / " + second);
        }
        return first / second;
    }

    @Override
    public Integer minus(Integer first) {
        if (first == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in -" + first);
        }
        return -first;
    }

    @Override
    public Integer abs(Integer first) {
        if (first == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in abs" + first);
        }
        return Math.abs(first);
    }

    @Override
    public Integer square(Integer first) {
        return mul(first, first);
    }

    @Override
    public Integer mod(Integer first, Integer second) {
        return first % second;
    }

    @Override
    public Integer parse(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public Integer parseInt(int value) {
        return value;
    }

}
