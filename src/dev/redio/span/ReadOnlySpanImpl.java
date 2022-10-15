package dev.redio.span;

import java.util.Objects;

public final class ReadOnlySpanImpl<E> implements ReadOnlySpan<E> {

    final E[] data;
    final int start;
    final int length;

    public ReadOnlySpanImpl(E[] data) {
        this(data, 0);
    }

    public ReadOnlySpanImpl(E[] data, int start) {
        this(data, start, data.length - start);
    }

    public ReadOnlySpanImpl(E[] data, int start, int end) {
        this.data = Objects.requireNonNull(data);
        this.start = Objects.checkFromToIndex(start, end, this.data.length);
        this.length = end - start;
    }

    public ReadOnlySpanImpl(SpanImpl<E> span) {
        Objects.requireNonNull(span);
        this.data = span.data;
        this.start = span.start;
        this.length = span.length;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, this.length);
        return this.data[index + this.start];
    }

    public SpanImpl<E> toSpan() {
        return new SpanImpl<>(this);
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public ReadOnlySpanImpl<E> subSequence(int start, int end) {
        return new ReadOnlySpanImpl<>(this.data, start, end);
    }

    @Override
    public ReadOnlySpanImpl<E> subSequence(int start) {
        return this.subSequence(start, this.length);
    }
}
