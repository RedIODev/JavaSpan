package dev.redio.spanOld;

public interface SpanBase {

    int length();

    SpanBase subSequence(int start);

    SpanBase subSequence(int start, int end);

    default boolean isEmpty() {
        return this.length() == 0;
    }
}
