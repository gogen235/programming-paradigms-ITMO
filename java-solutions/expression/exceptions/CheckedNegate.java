package expression.exceptions;

import expression.AllExpression;
import expression.Minus;

public class CheckedNegate extends Minus {

    public CheckedNegate(AllExpression first) {
        super(first);
    }

    @Override
    public int makeAction(int first) {
        if (first == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in -" + first);
        }
        return super.makeAction(first);
    }
}
