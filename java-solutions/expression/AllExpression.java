package expression;

public interface AllExpression extends TripleExpression {
    int getPriority();
    boolean getCommutative();
    int evaluate(int var);
    int evaluate(int x, int y, int z);
    double evaluate(double var);
    String toString();
    String toMiniString();
    boolean equals(Object elem);
    int hashCode();
}
