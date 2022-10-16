package dev.redio.spanOld;

public class OversizedSpanException extends IndexOutOfBoundsException {
    
    public OversizedSpanException() {
        super();
    }

    public OversizedSpanException(long length) {
        super("The Span of size " + length + " is larger then int.\nTry using the L method variants of span or check the isOversized() method before access.");
    }

    public OversizedSpanException(String s) {
        super(s);
    }
}
