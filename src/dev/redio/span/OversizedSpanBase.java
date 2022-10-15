package dev.redio.span;

public interface OversizedSpanBase extends SpanBase {
    
    long lengthL();

    SpanBase subSequence(long start);

    SpanBase subSequence(long start, long end);

    default boolean isOversized() {
        return this.length() == -1;
    }

    @Override
    default boolean isEmpty() {
        return this.lengthL() == 0;
    }
}
