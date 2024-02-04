package expression.generic;

import expression.exceptions.UnsupportedSymbolException;

public class GenericTabulator implements Tabulator {

    private static Calculate<? extends Number> getMode(String mode) {
        return switch (mode) {
            case "i" -> new CheckedIntegerCalculate();
            case "bi" -> new BigIntegerCalculate();
            case "d" -> new DoubleCalculate();
            case "u" -> new IntegerCalculate();
            case "p" -> new PrimeCalculate();
            case "s" -> new ShortCalculate();
            //:note: exception on wrong mod
            default -> throw new UnsupportedSymbolException("this mod is unsupported");
        };
    }

    private <T extends Number> Object[][][] genTabulate(Calculate<T> mode, String expression,
                                                        int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        ExpressionParser<T> parser = new ExpressionParser<>();
        AllExpression<T> parsedExpression = parser.parse(expression, mode);
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        ans[i][j][k] = parsedExpression.evaluate(mode.parseInt(x1 + i),
                                mode.parseInt(y1 + j), mode.parseInt(z1 + k));
                    } catch (ArithmeticException e) {
                        ans[i][j][k] = null;
                    }

                }
            }
        }
        return ans;
    }
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Calculate<? extends Number> calculateMode = getMode(mode);
        return genTabulate(calculateMode, expression, x1, x2, y1, y2, z1, z2);
    }
}
