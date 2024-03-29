package dev.redio.span;

import java.util.Arrays;
import java.util.Objects;
import java.util.RandomAccess;

sealed class ReadOnlyArraySpan<E> 
    extends ReadOnlyBaseSpan<E>
    implements RandomAccess
    permits ArraySpan<E> {

    protected final E[] data;

    public ReadOnlyArraySpan(E[] array) {
        this(array, 0, array.length);
    }

    public ReadOnlyArraySpan(E[] array, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, array.length);
        this.data = Objects.requireNonNull(array);
    }

    protected ReadOnlyArraySpan(int start, int length, E[] data) { // special constructor without bounds checking.
        super(start, length);
        this.data = data;
    }

    @Override
    public E get(int index) {
        return this.data[this.start + Objects.checkIndex(index, this.length)];
    }

    @Override
    public ReadOnlySpan<E> duplicate() {
        return new ReadOnlyArraySpan<>(this.start, this.length, this.data); // special constructor without bounds checking.
    }

    @Override
    public ReadOnlySpan<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ReadOnlyArraySpan<>(this.start + start, length, this.data); // special constructor without bounds checking.
    }

    @Override
    public E[] toArray() {
        return Arrays.copyOfRange(this.data, this.start, this.start + this.length);
    }

}
