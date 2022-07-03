package dev.redio.span;

import java.util.Objects;

final class ArraySpan<E> 
    extends AbstractSpan<E> {
    
    private final E[] data;
    
    public ArraySpan(E[] array) {
        this(array, 0, array.length);
    }

    public ArraySpan(E[] array, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, array.length);
        this.data = Objects.requireNonNull(array);
    }

    private ArraySpan(int start, int length, E[] data) { // special constructor without bounds checking.
        super(start, length);
        this.data = data;
    }

    @Override
    public E get(int index) {
        return data[this.start + Objects.checkIndex(index, this.length)];
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
    public Span<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ArraySpan<>(this.start + start, length, this.data); // special constructor without bounds checking.
    }
}
