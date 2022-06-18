package dev.redio.span;

import java.util.Arrays;
import java.util.Objects;

sealed class ReadOnlyArraySpan<T> 
    extends ReadOnlyBaseSpan<T> 
    permits ArraySpan<T> {

    protected final T[] data;

    public ReadOnlyArraySpan(T[] array) {
        this(array, 0, array.length);
    }

    public ReadOnlyArraySpan(T[] array, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, array.length);
        this.data = Objects.requireNonNull(array);
        
    }

    @Override
    public T get(int index) {
        return this.data[this.start + Objects.checkIndex(index, this.length)];
    }

    @Override
    public ReadOnlySpan<T> duplicate() {
        return new ReadOnlyArraySpan<>(this.data, this.start, this.length);
    }

    @Override
    public ReadOnlySpan<T> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ReadOnlyArraySpan<>(this.data, this.start + start, length);
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOfRange(this.data, this.start, this.start + this.length);
    }

}
