package dev.redio.spanOld;

public interface ReadOnlySpanOld<E> extends SpanBaseOld<E> {
    
    SpanOld<E> toSpan();
}
