package expression.exceptions;

import expression.AllExpression;
import expression.Log10;

public class CheckedLog10 extends Log10 {
    public CheckedLog10(AllExpression first) {
        super(first);
    }
    public int makeAction(int first) {
        if (first <= 0) {
            throw new ArithmeticException("- or 0 in log");
        }
        return super.makeAction(first);
    }

}
