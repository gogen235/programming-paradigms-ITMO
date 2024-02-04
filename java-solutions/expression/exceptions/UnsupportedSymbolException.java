package expression.exceptions;

public class UnsupportedSymbolException extends IllegalArgumentException {
    public UnsupportedSymbolException(String massage) {
        super(massage);
    }
}
