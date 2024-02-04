package expression;

public abstract class UnaryOperation implements AllExpression{
    private final AllExpression first;
    String operator;
    private final int priority;
    UnaryOperation(AllExpression first, String operator, int priority) {
        this.first = first;
        this.operator = operator;
        this.priority = priority;
    }
    public AllExpression getFirst() {
        return first;
    }

    @Override
    public int getPriority() {
        return priority;
    }
    public boolean getCommutative() {
        return true;
    }

    public abstract int makeAction(int first);
    public abstract double makeAction(double first);

    @Override
    public int evaluate(int var) {
        return makeAction(first.evaluate(var));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeAction(first.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double var) {
        return makeAction(first.evaluate(var));
    }
    @Override
    public String toString() {
        return  operator + "(" + first.toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (first.getPriority() < priority) {
            return operator + "(" + first.toMiniString() + ")";
        }
        return operator + " " + first.toMiniString();
    }
}
