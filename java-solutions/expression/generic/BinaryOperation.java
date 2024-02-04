package expression.generic;

public abstract class BinaryOperation<T extends Number> implements AllExpression<T> {
    private final AllExpression<T>first;
    private final AllExpression<T> second;
    String operator;
    private final int priority;
    private final boolean commutative;
    private final Calculate<T> mode;
    BinaryOperation(AllExpression<T> first, AllExpression<T> second, String operator,
                    int priority, boolean commutative, Calculate<T> mode) {
        this.first = first;
        this.second = second;
        this.operator = operator;
        this.priority = priority;
        this.commutative = commutative;
        this.mode = mode;
    }

    public AllExpression<T> getFirst() {
        return first;
    }

    public AllExpression<T> getSecond() {
        return second;
    }

    public int getPriority() {
        return priority;
    }
    public boolean getCommutative() {
        return commutative;
    }
    public Calculate<T> getMode() {
        return mode;
    }

    public abstract T makeAction(T first, T second);
    @Override
    public T evaluate(T x, T y, T z) {
        return makeAction(first.evaluate(x, y, z), second.evaluate(x, y, z));
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
                this.priority == 3 && second.getPriority() == 3 && !second.getCommutative(),
                second, str);
        return str.toString();
    }

    private void checkBracket(boolean isBracket, AllExpression<T> exp, StringBuilder str) {
        if (isBracket) {
            pushInStr("(", ")", str, exp);
        } else {
            pushInStr("", "", str, exp);
        }
    }
    private void pushInStr(String lBracket, String rBracket, StringBuilder str, AllExpression<T> exp) {
        str.append(lBracket);
        str.append(exp.toMiniString());
        str.append(rBracket);
    }

}
