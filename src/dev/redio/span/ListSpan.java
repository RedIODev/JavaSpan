package dev.redio.span;

import java.util.List;
import java.util.Objects;

final class ListSpan<E>
    extends AbstractSpan<E> {

    private final List<E> data;
    
    public ListSpan(List<E> list) {
        this(list,0,list.size());
    }

    public ListSpan(List<E> list, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, list.size());
        this.data = Objects.requireNonNull(list);
    }

    private ListSpan(int start, int length, List<E> data) { // special constructor without bounds checking.
        super(start, length);
        this.data = data;
    }

    @Override
    public E get(int index) {
        return data.get(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public void set(int index, E value) {
        this.data.set(this.start + Objects.checkIndex(index, this.length), value);
    }

    @Override
    public void clear() {
        this.fill(null);
    }

    @Override
    public void fill(E value) {
        for (int i = 0; i < this.length; i++)
            this.data.set(this.start + i, value);
    }

    @Override
    public Span<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new ListSpan<>(this.start + start, length, this.data); // special constructor without bounds checking.
    }
}
