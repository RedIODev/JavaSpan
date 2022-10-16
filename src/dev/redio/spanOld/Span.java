package dev.redio.spanOld;

public interface Span<E> extends ObjectSpanBase<E> {

    void set(int index, E value);

    @Override
    E get(int index);

    ReadOnlySpan<E> asReadOnlySpan();

    void fill(E value);

    void clear();

    @Override
    int length();

    @Override
    Span<E> subSequence(int start, int end);

    @Override
    Span<E> subSequence(int start);
}
