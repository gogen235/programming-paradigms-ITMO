package expression.generic;

import expression.exceptions.OverflowException;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.List;

public class ExpressionParser<T extends Number> {
    public AllExpression<T> parse(final String source, Calculate<T> mode) {
        return parse(new StringSource(source), mode);
    }

    public AllExpression<T> parse(final CharSource source, Calculate<T> mode) {
        return new Parser(source, mode).parse();
    }

    private class Parser extends BaseParser {
        private final Calculate<T> mode;
        private static final int maxPriority = 4;
        protected Parser(CharSource source, Calculate<T> mode) {
            super(source);
            this.mode = mode;
        }

        private AllExpression<T> parse() {
            AllExpression<T> result = parseNPriority(1);
            if (!eof()) {
                if (test(')')) {
                    throw error("opening parenthesis missing");
                } else {
                    throw error("unexpected symbol");
                }
            }
            return result;
        }
        private AllExpression<T> parseMaxPriority() {
            skipWhitespace();
            AllExpression<T> result;
            if (take('-')) {
                if (between('0', '9')) {
                    result = parseConst(true);
                } else {
                    result = new Minus<>(parseMaxPriority(), mode);
                }
            } else if (take("abs")) {
                result = parseUnaryOperation("abs");
            } else if (take("square")) {
                result = parseUnaryOperation("square");
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
        private AllExpression<T> parseNPriority(int n) {
            if (n == maxPriority) {
                return parseMaxPriority();
            } else {
                AllExpression<T> result = parseNPriority(n + 1);
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

        private AllExpression<T> parseUnaryOperation(String operation) {
            if (checkAfterOperation()) {
                throw error("no whitespace after operation");
            }
            return getExpression(operation, parseMaxPriority(), null);
        }

        private AllExpression<T> parseVariable() {
            StringBuilder sb = new StringBuilder();
            while (isVar()) {
                sb.append(take());
            }
            if (sb.length() > 1) {
                throw error("unsupported variable");
            }
            return new Variable<>(sb.toString());
        }
        private AllExpression<T> parseConst(boolean minus) {
            StringBuilder sb = new StringBuilder();
            if (minus) {
                sb.append('-');
            }
            while (between('0', '9')) {
                sb.append(take());
            }
            try {
                return new Const<>(mode.parse(sb.toString()));
            } catch (NumberFormatException e) {
                throw new OverflowException("constant overflow " + sb);
            }
        }
        private AllExpression<T> getExpression(String sign, AllExpression<T> first, AllExpression<T> second) {
            return switch (sign) {
                case "+" -> new Add<>(first, second, mode);
                case "-" -> new Subtract<>(first, second, mode);
                case "*" -> new Multiply<>(first, second, mode);
                case "/" -> new Divide<>(first, second, mode);
                case "mod" -> new Mod<>(first, second, mode);
                case "abs" -> new Abs<>(first, mode);
                case "square" -> new Square<>(first, mode);
                default -> null;
            };
        }
        private List<String> getOperation(int priority) {
            return switch (priority) {
                case 1 -> List.of("+", "-");
                case 2 -> List.of("*", "/", "mod");
                case 3 -> List.of("abs", "square");
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
