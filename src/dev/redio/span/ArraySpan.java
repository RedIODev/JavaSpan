package dev.redio.span;

import java.util.Objects;

final class ArraySpan<E> 
    extends ReadOnlyArraySpan<E> 
    implements Span<E> {

    public ArraySpan(E[] array) {
        super(array, 0, array.length);
    }

    public ArraySpan(E[] array, int start, int length) {
        super(array, start, length);
    }

    private ArraySpan(int start, int length, E[] data) { // special constructor without bounds checking.
        super(start, length, data);
    }
    @Override
    public void set(int index, E value) {
        this.data[this.start + Objects.checkIndex(index, this.length)] = value;
    }

    @Override
    public void clear() {
        this.fill(null);
    }

    @Override
    public void fill(E value) {
        for (int i = 0; i < this.length; i++)
            this.data[this.start + i] = value;
    }

    @Override
    public Span<E> duplicate() {
        return new ArraySpan<>(this.start, this.length, this.data); // special constructor without bounds checking.
    }

    @Override
    public Span<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ArraySpan<>(this.start + start, length, this.data); // special constructor without bounds checking.
    }
}
