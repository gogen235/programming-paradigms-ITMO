package expression.parser;

import expression.exceptions.UnsupportedSymbolException;

public class BaseParser {
    private static final char END = 0;
    private final CharSource source;
    private char ch = 0xffff;
    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }
    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }
    protected boolean takeWhitespace() {
        if (Character.isWhitespace(ch)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }
    protected boolean take(final String expected) {
        int pos = source.getPos();
        for (int i = 0; i < expected.length(); i++) {
            if (test(expected.charAt(i))) {
                take();
            } else {
                source.assignPos(pos);
                return false;
            }
        }
        return true;
    }
    protected boolean eof() {
        return ch == END;
    }
    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
    protected UnsupportedSymbolException error(final String message) {
        return source.error(message);
    }
}