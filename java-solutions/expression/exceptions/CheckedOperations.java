package expression.exceptions;

public class CheckedOperations {
    public static int mul(int first, int second, String massage) {
        if (second == 0 || first == 0) {
            return 0;
        }
        int result = first * second;
        if (result / first == second && result / second == first) {
            return result;
        }
        throw new OverflowException(massage);
    }

    public static int abs(int num) {
        if (num < 0) {
            return -num;
        }
        return num;
    }
}
