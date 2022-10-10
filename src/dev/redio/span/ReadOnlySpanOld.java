package dev.redio.span;

public interface ReadOnlySpanOld<E> extends SpanBaseOld<E> {
    
    SpanOld<E> toSpan();
}
