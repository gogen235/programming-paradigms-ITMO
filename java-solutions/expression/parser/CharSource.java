package expression.parser;

import expression.exceptions.UnsupportedSymbolException;

public interface CharSource {
    boolean hasNext();
    char next();
    int getPos();
    void assignPos(int pos);
    UnsupportedSymbolException error(final String message);
}
