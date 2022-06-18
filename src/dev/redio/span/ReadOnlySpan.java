package dev.redio.span;

public interface ReadOnlySpan<T> 
    extends Iterable<T> {

    static <T> ReadOnlySpan<T> of(T[] array) {
        return new ReadOnlyArraySpan<>(array);
    }

    static <T> ReadOnlySpan<T> of(T[] array, int start, int length) {
        return new ReadOnlyArraySpan<>(array, start, length);
    }

    static StringSpan of(String string) {
        return new StringSpan(string);
    }

    static StringSpan of(String string, int start, int length) {
        return new StringSpan(string, start, length);
    }
    
    T get(int index);

    int length();

    ReadOnlySpan<T> duplicate();

    default boolean isEmpty() {
        return this.length() == 0;
    }

    default void copyTo(Span<T> destination) {
        if (!this.tryCopyTo(destination))
            throw new IllegalArgumentException("The destination Span is shorter that the source Span.");
    }

    default boolean tryCopyTo(Span<T> destination) {
        if (destination == null || this.length() > destination.length())
            return false;
        for (int i = 0; i < this.length(); i++)
            destination.set(i, this.get(i));
        return true;
    }

    default ReadOnlySpan<T> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    ReadOnlySpan<T> slice(int start, int length);

    Object[] toArray();

    default T[] toArray(T[] array) {
        return Spans.toArray(this, array);
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}