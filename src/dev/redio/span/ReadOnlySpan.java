package dev.redio.span;

public interface ReadOnlySpan<E> extends SpanBase<E> {
    
    Span<E> toSpan();
}
