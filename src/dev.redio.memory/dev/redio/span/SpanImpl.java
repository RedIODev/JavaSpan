package dev.redio.span;

import java.util.Objects;

public final class SpanImpl<E> implements Span<E> {
    
    final E[] data;
    final int start;
    final int length;

    public SpanImpl(E[] data, int start, int length) {
        this.data = Objects.requireNonNull(data);
        Objects.checkFromIndexSize(start, length, data.length);
        this.start = start;
        this.length = length;
    }

    @Override
    public SpanImpl<E> slice(long start, long length) {
        return new SpanImpl<>(this.data, Math.toIntExact(start), Math.toIntExact(length));
    }

    public long length() {
        return this.length;
    }

    public void set(long index, E value) {
        Objects.checkIndex(index, this.length);
        this.data[(int)index + this.start] = value;
    }

    public E get(long index) {
        Objects.checkIndex(index, this.length);
        return this.data[(int)index + this.start];
    }
}
