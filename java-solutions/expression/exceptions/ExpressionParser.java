package expression.exceptions;

import expression.AllExpression;
import expression.Const;
import expression.Variable;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.List;

public class ExpressionParser implements TripleParser {
    public AllExpression parse(final String source) {
        return parse(new StringSource(source));
    }
    public static AllExpression parse(final CharSource source) {
        return new Parser(source).parse();
    }
    private static class Parser extends BaseParser {
        private static final int maxPriority = 4;
        protected Parser(CharSource source) {
            super(source);
        }

        private AllExpression parse() {
            AllExpression result = parseNPriority(1);
            if (!eof()) {
                if (test(')')) {
                    throw error("opening parenthesis missing");
                } else {
                    throw error("unexpected symbol");
                }
            }
            return result;
        }
        private AllExpression parseMaxPriority() {
            skipWhitespace();
            AllExpression result;
            if (take('-')) {
                if (between('0', '9')) {
                    result = parseConst(true);
                } else {
                    result = new CheckedNegate(parseMaxPriority());
                }
            } else if (take("reverse")) {
                result = parseUnaryOperation("reverse");
            } else if (take("pow10")) {
                result = parseUnaryOperation("pow10");
            } else if (take("log10")) {
                result = parseUnaryOperation("log10");
            } else if (between('0', '9')) {
                result = parseConst(false);
            } else if (isVar()) {
                result = parseVariable();
            } else if (take('(')) {
                result = parseNPriority(1);
                if (!take(')')) {
                    throw error("closing parenthesis missing");
                }
            } else {
                throw error("argument missing");
            }
            skipWhitespace();
            return result;
        }
        private AllExpression parseNPriority(int n) {
            if (n == maxPriority) {
                return parseMaxPriority();
            } else {
                AllExpression result = parseNPriority(n + 1);
                loop: while (true) {
                    for (String operation : getOperation(n)) {
                        if (take(operation)) {
                            if (operation.length() > 1 && checkAfterOperation()) {
                                throw error("no whitespace after operation");
                            }
                            result = getExpression(operation, result, parseNPriority(n + 1));
                            continue loop;
                        }
                    }
                    return result;
                }
            }
        }

        private AllExpression parseUnaryOperation(String operation) {
            if (checkAfterOperation()) {
                throw error("no whitespace after operation");
            }
            return getExpression(operation, parseMaxPriority(), null);
        }

        private AllExpression parseVariable() {
            StringBuilder sb = new StringBuilder();
            while (isVar()) {
                sb.append(take());
            }
            if (sb.length() > 1) {
                throw error("unsupported variable");
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
            try {
                return new Const(Integer.parseInt(sb.toString()));
            } catch (NumberFormatException e) {
                throw new OverflowException("constant overflow " + sb);
            }
        }
        private AllExpression getExpression(String sign, AllExpression first, AllExpression second) {
            return switch (sign) {
                case "gcd" -> new CheckedGCD(first, second);
                case "lcm" -> new CheckedLCM(first, second);
                case "+" -> new CheckedAdd(first, second);
                case "-" -> new CheckedSubtract(first, second);
                case "*" -> new CheckedMultiply(first, second);
                case "/" -> new CheckedDivide(first, second);
                case "pow10" -> new CheckedPow10(first);
                case "log10" -> new CheckedLog10(first);
                case "reverse" -> new CheckedReverse(first);
                default -> null;
            };
        }
        private List<String> getOperation(int priority) {
            return switch (priority) {
                case 1 -> List.of("gcd", "lcm");
                case 2 -> List.of("+", "-");
                case 3 -> List.of("*", "/");
                case 4 -> List.of("reverse", "pow10", "log10");
                default -> null;
            };
        }
        protected void skipWhitespace() {
            while (takeWhitespace());
        }
        private boolean isVar() {
            return between('x', 'z');
        }
        private boolean checkAfterOperation() {
            return !takeWhitespace() && !test('-') && !test('(');
        }

    }

}
