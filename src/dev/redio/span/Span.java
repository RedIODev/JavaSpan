package dev.redio.span;

public interface Span<T> 
    extends ReadOnlySpan<T> {

    static <T> Span<T> of(T[] array) {
        return new ArraySpan<>(array);
    }

    static <T> Span<T> of(T[] array, int start, int end) {
        return new ArraySpan<>(array, start, end);
    }
    
    void set(int index, T value);

    void clear();

    void fill(T value);

    @Override
    Span<T> duplicate();

    @Override
    default Span<T> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    Span<T> slice(int start, int length);
}
