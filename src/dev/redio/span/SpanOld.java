package dev.redio.span;

import dev.redio.span.primitive.IntSpanArrayOld;

public interface SpanOld<E> extends SpanBaseOld<E> {

    static IntSpanArrayOld ofA(int[] array) {
        return new IntSpanArrayOld(array);
    }

    void setObj(int index, E value);

    void setObj(long index, E value);
    
    ReadOnlySpanOld<E> readOnlySpan();

    void fill();
}
