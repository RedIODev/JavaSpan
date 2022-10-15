package dev.redio.span;

import java.util.function.IntFunction;

public final class Spans {
    
    private Spans() {}

    static <T, E> T[] toObjArray(IntFunction<T[]> generator, ObjectSpanBase<E> span) {
        var array = generator.apply(span.length());
        return toObjArray(array, span);
    }

    @SuppressWarnings("unchecked")
    static <T, E> T[] toObjArray(T[] dest, ObjectSpanBase<E> span) {
        for (int i = 0; i < dest.length; i++) 
            dest[i] = (T)span.get(i);
        return dest;
    }

    public static <S extends SpanBase> S checkSpanSize(S span) {
        if (span instanceof PrimitiveSpanBase ps && ps.isOversized())
            throw new OversizedSpanException(ps.lengthL());
        return span;
    }
}
