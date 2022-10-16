package dev.redio.spanOld;

import java.lang.reflect.Array;
import java.util.Objects;

public final class SpanImpl<E> implements Span<E> {
    
    final E[] data;
    final int start;
    final int length;

    public SpanImpl(E[] data) {
        this(data, 0);
    }

    public SpanImpl(E[] data, int start) {
        this(data, start, data.length - start);
    }

    public SpanImpl(E[] data, int start, int end) {
        this.data = Objects.requireNonNull(data);
        this.start = Objects.checkFromToIndex(start, end, this.data.length);
        this.length = end - start;
    }

    @SuppressWarnings("unchecked")
    public SpanImpl(ReadOnlySpanImpl<E> span) {
        Objects.requireNonNull(span);
        E[] newData = (E[])Array.newInstance(span.data.getClass().getComponentType(), span.length);
        System.arraycopy(span.data, span.start, newData, 0, span.length);
        this.data = newData;
        this.length = newData.length;
        this.start = 0;
    }

    @Override
    public void set(int index, E value) {
        Objects.checkIndex(index, this.length);
        this.data[index + this.start] = value;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, this.length);
        return this.data[index + this.start];
    }

    public ReadOnlySpan<E> asReadOnlySpan() {
        return new ReadOnlySpanImpl<>(this.data, this.start, this.length);
    }

    @Override
    public void fill(E value) {
        for (int i = 0; i < this.length; i++) 
            this.data[i] = value;
    }

    @Override
    public void clear() {
        this.fill(null);
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public SpanImpl<E> subSequence(int start, int end) {
        return new SpanImpl<>(this.data, start, end);
    }

    @Override
    public SpanImpl<E> subSequence(int start) {
        return this.subSequence(start, this.length);
    }
}
