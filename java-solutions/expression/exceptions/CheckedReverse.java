package expression.exceptions;

import expression.AllExpression;
import expression.Reverse;

public class CheckedReverse extends Reverse {
    public CheckedReverse(AllExpression first) {
        super(first);
    }
    @Override
    public int makeAction(int first) {
        String absNum;
        try {
            if (first < 0) {
                absNum = Integer.toString(first).substring(1);
                return -Integer.parseInt(new StringBuilder(absNum).reverse().toString());
            } else {
                absNum = Integer.toString(first);
                return Integer.parseInt(new StringBuilder(absNum).reverse().toString());
            }
        } catch (NumberFormatException e) {
            throw new OverflowException("overflow in reverse " + first);
        }
    }

}
