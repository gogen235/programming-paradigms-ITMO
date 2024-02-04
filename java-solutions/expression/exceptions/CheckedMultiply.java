package expression.exceptions;

import expression.AllExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(AllExpression first, AllExpression second) {
        super(first, second);
    }

    @Override
    public int makeAction(int first, int second) {
        String massage = "overflow in " + first + " * " + second;
        return CheckedOperations.mul(first, second, massage);
    }
}
