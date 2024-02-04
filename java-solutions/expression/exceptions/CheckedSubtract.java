package expression.exceptions;

import expression.AllExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {

    public CheckedSubtract(AllExpression first, AllExpression second) {
        super(first, second);
    }
    @Override
    public int makeAction(int first, int second) {
        if (second > 0 && first < Integer.MIN_VALUE + second || second < 0 && first > Integer.MAX_VALUE + second) {
            throw new OverflowException("overflow in " + first + " - " + second);
        }
        return super.makeAction(first, second);
    }
}
