package dev.redio.span.buffer;

import java.nio.Buffer;
import java.util.Collection;

import dev.redio.span.Span;

public interface BufferSpan<E, B extends Buffer> {

    int length();

    default void clear() {
        this.fill(null);
    }

    void fill(E value);

    default boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    default boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!this.contains(e))
                return false;
        return true;
    }

    int indexOf(Object o);

    int lastIndexOf(Object o);

    default boolean isEmpty() {
        return this.length() == 0;
    }

    default BufferSpan<E,B> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    BufferSpan<E,B> slice(int start, int length);

    Span<E> span();

    B buffer();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
