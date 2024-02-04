package expression.exceptions;

import expression.AllExpression;
import expression.Pow10;

public class CheckedPow10 extends Pow10 {
    CheckedPow10(AllExpression first) {
        super(first);
    }
    @Override
    public int makeAction(int first) {
        if (first < 0) {
            throw new ArithmeticException("- in pow");
        }
        int result = 1;
        String massage = "overflow in pow10 " + first;
        for (int i = 0; i < first; i++) {
            result = CheckedOperations.mul(result, 10, massage);
        }
        return result;
    }
}
