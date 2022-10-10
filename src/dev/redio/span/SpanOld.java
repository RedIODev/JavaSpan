package dev.redio.span;

import dev.redio.span.primitive.IntSpanArray;

public interface SpanOld<E> extends SpanBaseOld<E> {

    static IntSpanArray ofA(int[] array) {
        return new IntSpanArray(array);
    }

    void setObj(int index, E value);

    void setObj(long index, E value);
    
    ReadOnlySpanOld<E> readOnlySpan();

    void fill();
}
