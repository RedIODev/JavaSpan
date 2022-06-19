package dev.redio.span;

import java.util.List;

public interface Span<E> 
    extends ReadOnlySpan<E> {

    static <T> Span<T> of(T[] array) {
        return new ArraySpan<>(array);
    }

    static <T> Span<T> of(T[] array, int start, int end) {
        return new ArraySpan<>(array, start, end);
    }

    static <T> Span<T> of(List<T> list) {
        return new ListSpan<>(list);
    }

    static <T> Span<T> of(List<T> list, int start, int length) {
        return new ListSpan<>(list, start, length);
    }
    
    void set(int index, E value);

    void clear();

    void fill(E value);

    @Override
    Span<E> duplicate();

    @Override
    default Span<E> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    Span<E> slice(int start, int length);
}
