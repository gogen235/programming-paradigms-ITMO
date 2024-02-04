package expression;

public abstract class BinaryOperation implements AllExpression {
    private final AllExpression first;
    private final AllExpression second;
    String operator;
    private final int priority;
    private final boolean commutative;
    BinaryOperation(AllExpression first, AllExpression second, String operator, int priority, boolean commutative) {
        this.first = first;
        this.second = second;
        this.operator = operator;
        this.priority = priority;
        this.commutative = commutative;
    }

    public AllExpression getFirst() {
        return first;
    }

    public AllExpression getSecond() {
        return second;
    }

    public int getPriority() {
        return priority;
    }
    public boolean getCommutative() {
        return commutative;
    }

    @Override
    public int evaluate(int var) {
        return makeAction(first.evaluate(var), second.evaluate(var));
    }
    public abstract int makeAction(int first, int  second);
    public abstract double makeAction(double first, double second);
    @Override
    public int evaluate(int x, int y, int z) {
        return makeAction(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }
    @Override
    public double evaluate(double var) {
        return makeAction(first.evaluate(var), second.evaluate(var));
    }
    @Override
    public String toString() {
        return "(" + first.toString() + " " + operator + " " + second.toString() + ")";
    }

    @Override
    public String toMiniString() {
        StringBuilder str = new StringBuilder();
        checkBracket(this.priority > first.getPriority(), first, str);
        str.append(" ");
        str.append(operator);
        str.append(" ");
        checkBracket(this.priority > second.getPriority() ||
                !this.commutative && this.priority == second.getPriority() ||
                this.priority == 3 && second.getPriority() == 3 && !second.getCommutative() ||
                (this instanceof GCD && second instanceof LCM || this instanceof LCM && second instanceof GCD), second, str);
        return str.toString();
    }

    private void checkBracket(boolean isBracket, AllExpression exp, StringBuilder str) {
        if (isBracket) {
            pushInStr("(", ")", str, exp);
        } else {
            pushInStr("", "", str, exp);
        }
    }
    private void pushInStr(String lBracket, String rBracket, StringBuilder str, AllExpression exp) {
            str.append(lBracket);
            str.append(exp.toMiniString());
            str.append(rBracket);
    }
    @Override
    public boolean equals(Object elem) {
        if (elem instanceof BinaryOperation newElem) {
            return first.equals(newElem.getFirst()) && second.equals(newElem.getSecond()) &&
                    this.getClass().equals(newElem.getClass());
        }
        return false;
    }
}
