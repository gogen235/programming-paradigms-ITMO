package expression.parser;

import expression.*;
import expression.exceptions.TripleParser;

import java.util.List;

public class ExpressionParser implements TripleParser {
    public AllExpression parse(final String source) {
        return parse(new StringSource(source));
    }

    public static AllExpression parse(final CharSource source) {
        return new Parser(source).parseNPriority(1);
    }

    private static class Parser extends BaseParser {
        private static final int maxPriority = 4;

        protected Parser(CharSource source) {
            super(source);
        }

        private AllExpression parseMaxPriority() {
            skipWhitespace();
            AllExpression result;
            if (take('-')) {
                if (between('0', '9')) {
                    result = parseConst(true);
                } else {
                    result = new Minus(parseMaxPriority());
                }
            } else if (take("reverse")) {
                result = new Reverse(parseMaxPriority());
            } else if (between('0', '9')) {
                result = parseConst(false);
            } else if (isLetter()) {
                result = parseVariable();
            } else {
                take('(');
                result = parseNPriority(1);
                take(')');
            }
            skipWhitespace();
            return result;
        }

        private AllExpression parseNPriority(int n) {
            if (n == maxPriority) {
                return parseMaxPriority();
            } else {
                AllExpression result = parseNPriority(n + 1);
                loop:
                while (true) {
                    for (String operation : getOperation(n)) {
                        if (take(operation)) {
                            result = getExpression(operation, result, parseNPriority(n + 1));
                            continue loop;
                        }
                    }
                    return result;
                }
            }
        }

        private AllExpression parseVariable() {
            StringBuilder sb = new StringBuilder();
            while (isLetter()) {
                sb.append(take());
            }
            return new Variable(sb.toString());
        }

        private AllExpression parseConst(boolean minus) {
            StringBuilder sb = new StringBuilder();
            if (minus) {
                sb.append('-');
            }
            while (between('0', '9')) {
                sb.append(take());
            }
            return new Const(Integer.parseInt(sb.toString()));
        }

        private AllExpression getExpression(String sign, AllExpression first, AllExpression second) {
            return switch (sign) {
                case "gcd" -> new GCD(first, second);
                case "lcm" -> new LCM(first, second);
                case "+" -> new Add(first, second);
                case "-" -> new Subtract(first, second);
                case "*" -> new Multiply(first, second);
                case "/" -> new Divide(first, second);
                default -> null;
            };
        }

        private List<String> getOperation(int priority) {
            return switch (priority) {
                case 1 -> List.of("gcd", "lcm");
                case 2 -> List.of("+", "-");
                case 3 -> List.of("*", "/");
                default -> null;
            };
        }

        protected void skipWhitespace() {
            while (takeWhitespace()) ;
        }

        private boolean isLetter() {
            return between('a', 'z') || between('A', 'Z');
        }
    }

}
