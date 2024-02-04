package expression;

public class Log10 extends UnaryOperation {
    public Log10(AllExpression first) {
        super(first, "log10", 4);
    }
    @Override
    public int makeAction(int first) {
        int num = first;
        int result = 0;
        while (num > 9) {
            num /= 10;
            result++;
        }
        return result;
    }

    @Override
    public double makeAction(double first) {
        return 0;
    }
}
