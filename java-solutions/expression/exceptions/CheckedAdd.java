package expression.exceptions;

import expression.Add;
import expression.AllExpression;

public class CheckedAdd extends Add {

    public CheckedAdd(AllExpression first, AllExpression second) {
        super(first, second);
    }

    @Override
    public int makeAction(int first, int second) {
        int result = first + second;
        if (((first ^ result) & (second ^ result)) < 0) {
            throw new OverflowException("overflow in " + first + " + " + second);
        }
        return result;
    }
}
