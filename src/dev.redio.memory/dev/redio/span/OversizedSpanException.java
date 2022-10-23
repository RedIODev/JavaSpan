package dev.redio.span;

public class OversizedSpanException extends IndexOutOfBoundsException {
    
    public OversizedSpanException() {
        super();
    }

    public OversizedSpanException(long length) {
        super("The Span of size " + length + " is larger then int.");
    }

    public OversizedSpanException(String s) {
        super(s);
    }
}
