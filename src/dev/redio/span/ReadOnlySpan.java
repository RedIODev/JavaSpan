package dev.redio.span;

public interface ReadOnlySpan<E> extends ObjectSpanBase<E> {
    
    Span<E> toSpan();

    @Override
    ReadOnlySpan<E> subSequence(int start, int end);

    @Override
    ReadOnlySpan<E> subSequence(int start);
}
