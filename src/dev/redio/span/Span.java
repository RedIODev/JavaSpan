package dev.redio.span;

public interface Span<E> extends SpanBase<E> {

    void setObj(int index, E value);
    
    ReadOnlySpan<E> readOnlySpan();
}
