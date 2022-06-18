package dev.redio.span;

import java.util.Objects;

final class ArraySpan<T> 
    extends ReadOnlyArraySpan<T> 
    implements Span<T> {

    public ArraySpan(T[] array) {
        super(array, 0, array.length);
    }

    public ArraySpan(T[] array, int start, int length) {
        super(array, start, length);
    }
    @Override
    public void set(int index, T value) {
        this.data[this.start + Objects.checkIndex(index, this.length)] = value;
    }

    @Override
    public void clear() {
        this.fill(null);
    }

    @Override
    public void fill(T value) {
        for (int i = 0; i < this.length; i++)
            this.data[this.start + i] = value;
    }

    @Override
    public Span<T> duplicate() {
        return new ArraySpan<>(this.data, this.start, this.length);
    }

    @Override
    public Span<T> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ArraySpan<>(this.data, this.start + start, length);
    }
}
