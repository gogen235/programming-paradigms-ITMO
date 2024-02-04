package expression.exceptions;

import expression.AllExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(AllExpression first, AllExpression second) {
        super(first, second);
    }

    @Override
    public int makeAction(int first, int second) {
        if (second == 0) {
            throw new DivisionByZeroException("division by null");
        } else if (first == Integer.MIN_VALUE && second == -1) {
            throw new OverflowException("overflow in " + first + " / " + second);
        }
        return super.makeAction(first, second);
     }
}
