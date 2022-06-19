package dev.redio.span;

import java.util.List;
import java.util.Objects;

sealed class ReadOnlyListSpan<E>
    extends ReadOnlyBaseSpan<E>
    permits ListSpan<E> {
    
    protected final List<E> data;

    public ReadOnlyListSpan(List<E> list) {
        this(0, list.size(), Objects.requireNonNull(list)); // special constructor without bounds checking.
    }

    public ReadOnlyListSpan(List<E> list, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, list.size());
        this.data = Objects.requireNonNull(list);
    }
    
    protected ReadOnlyListSpan(int start, int length, List<E> data) { // special constructor without bounds checking.
        super(start, length);
        this.data = data;
    }

    @Override
    public E get(int index) {
        return this.data.get(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public ReadOnlySpan<E> duplicate() {
        return new ReadOnlyListSpan<>(this.start, this.length, this.data); // special constructor without bounds checking.
    }

    @Override
    public ReadOnlySpan<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ReadOnlyListSpan<>(this.start + start, length, this.data); // special constructor without bounds checking.
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.length];
        for (int i = 0; i < this.length; i++) 
            array[i] = this.data.get(this.start + i);
        return array;
    }
    
}
