package dev.redio.span;

import dev.redio.span.primitive.IntSpanArray;

public interface Span<E> extends SpanBase<E> {

    static IntSpanArray ofA(int[] array) {
        return new IntSpanArray(array);
    }

    void setObj(int index, E value);
    
    ReadOnlySpan<E> readOnlySpan();
}
