package expression.parser;

import expression.exceptions.UnsupportedSymbolException;

public class StringSource implements CharSource{
    private final String data;
    private int pos;
    public int getPos() {
        return pos;
    }
    public void assignPos(int pos) {
        this.pos = pos;
    }
    public StringSource(final String data) {
        this.data = data;
    }
    @Override
    public boolean hasNext() {
        return pos < data.length();
    }
    @Override
    public char next() {
        return data.charAt(pos++);
    }
    @Override
    public UnsupportedSymbolException error(final String message) {
        return new UnsupportedSymbolException(pos + ": " + message);
    }
}
