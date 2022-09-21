package dev.redio.span;

import java.util.function.IntFunction;

public final class Spans {
    
    private Spans() {}

    static <T, E> T[] toObjArray(IntFunction<T[]> generator, SpanBase<E> span) {
        var array = generator.apply(span.size());
        return toObjArray(array, span);
    }

    @SuppressWarnings("unchecked")
    static <T, E> T[] toObjArray(T[] dest, SpanBase<E> span) {
        for (int i = 0; i < dest.length; i++) 
            dest[i] = (T)span.getObj(i);
        return dest;
    }
}
