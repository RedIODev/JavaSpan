package dev.redio.span;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class SpanImpl<E> implements Iterable<E> {
    
    final E[] data;
    final int start;
    final int length;

    public SpanImpl(E[] data, int start, int end) {
        this.data = Objects.requireNonNull(data);
        Objects.checkFromToIndex(start, end, this.data.length);
        this.start = start;
        this.length = end - start;
    }

    public int length() {
        return this.length;
    }

    public void set(int index, E value) {
        Objects.checkIndex(index, this.length);
        this.data[index + this.start] = value;
    }

    public E get(int index) {
        Objects.checkIndex(index, this.length);
        return this.data[index + this.start];
    }

    public ReadOnlySpan<E> asReadOnlySpan() {

    }

    public void clear() {
        this.fill(null);
    }

    public void fill(E value) {
        for (int i = 0; i < this.length; i++) 
            this.data[i + this.start] = value;
    }

    public <T> boolean contains(T obj) {
        for (E e : this) 
            if (Objects.equals(e, obj))
                return true;
        return false;
    }

    public <T> boolean containsAll(Iterable<? extends T> i) {
        for (T t : i) 
            if (this.contains(t))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        
    }

    @Override
    public Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            int i = 0;
            @Override
            public boolean hasNext() {
                return this.i < SpanImpl.this.length();
            }

            @Override
            public E next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();
                return SpanImpl.this.get(i++);
            }
        }

        return new Iter();
    }

    @Override
    public Spliterator<E> spliterator() {
        class Splitter implements Spliterator<E> {
            private final int size;
            private int index = 0;

            Splitter() {
                this(SpanImpl.this.length(), 0);
            }

            Splitter(int size, int index) {
                this.size = size;
                this.index = index;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.size)
                    return false;
                action.accept(SpanImpl.this.get(this.index++));
                return true;
            }

            public Spliterator<E> trySplit() {
                int newIndex = this.index;
                int midPoint = (this.index + this.size) >>> 1;
                if (this.index >= midPoint)
                    return null;
                this.index = midPoint;
                return new Splitter(newIndex, midPoint);
            }

            public long estimateSize() {
                return (this.size - this.index);
            }

            public int characteristics() {
                return ORDERED | SIZED | SUBSIZED;
            }
        }

        return new Splitter();
    }
}
