package expression;

public class Variable implements AllExpression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getPriority() {
        return 4;
    }
    public boolean getCommutative() {
        return true;
    }
    @Override
    public int evaluate(int var) {
        return var;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> 0;
        };
    }
    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public boolean equals(Object elem) {
        if (elem instanceof Variable newElem) {
            return newElem.getName().equals(name);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
