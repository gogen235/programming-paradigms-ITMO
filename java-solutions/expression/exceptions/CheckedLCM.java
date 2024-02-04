package expression.exceptions;

import expression.AllExpression;
import expression.LCM;

public class CheckedLCM extends LCM {
    public CheckedLCM(AllExpression first, AllExpression second) {
        super(first, second);
    }
    @Override
    public int makeAction(int first, int second) {
        if (gcd(first, second) == 0) {
            return 0;
        }
        String massage = "overflow in " + first + " lcm " + second;
        return CheckedOperations.mul(first / CheckedOperations.abs(gcd(first, second)), second, massage);
    }

}
