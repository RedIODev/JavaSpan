package dev.redio.span;

import java.util.function.IntFunction;

public final class Spans {
    
    private Spans() {}

    static <T, E> T[] toObjArray(IntFunction<T[]> generator, SpanBase<E> span) {
        var array = generator.apply(span.length());
        return toObjArray(array, span);
    }

    @SuppressWarnings("unchecked")
    static <T, E> T[] toObjArray(T[] dest, SpanBase<E> span) {
        for (int i = 0; i < dest.length; i++) 
            dest[i] = (T)span.getObj(i);
        return dest;
    }

    public static <S extends SpanBase<?>> S checkSpanSize(S span) {
        if (span.isOverSized())
            throw new OversizedSpanException(span.lengthL());
        return span;
    }
}
