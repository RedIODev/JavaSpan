package dev.redio.span;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.redio.utils.Streamable;

public interface SpanBase<E> extends Streamable<E> {

    long length();

    E get(long index);

    default boolean isEmpty() {
        return this.length() == 0;
    }

    default boolean contains(Object obj) {
        return this.indexOf(obj) != -1;
    }

    default boolean containsAll(Iterable<?> i) {
        for (var t : i) 
            if (this.contains(t))
                return true;
        return false;
    }

    default long indexOf(Object obj) {
        final long length = this.length();
        for (long i = 0; i < length; i++) 
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    default long lastIndexOf(Object obj) {
        for (long i = this.length(); i >= 0; i--) 
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    @SuppressWarnings("unchecked")
    default <T> T[] toArray(IntFunction<T[]> generator) {
        final long length = this.length();
        if (length > Integer.MAX_VALUE)
            throw new OversizedSpanException(length);
        T[] array = generator.apply((int)length);
        for (int i = 0; i < array.length; i++)
            array[i] = (T)this.get(i);
        return array;
    }

    @Override
    default Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            long i = 0;
            @Override
            public boolean hasNext() {
                return this.i < SpanBase.this.length();
            }

            @Override
            public E next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();
                return SpanBase.this.get(i++);
            }
        }

        return new Iter();
    }

    @Override
    default Spliterator<E> spliterator() {
        class Splitter implements Spliterator<E> {
            private final long size;
            private long index = 0;

            Splitter() {
                this(SpanBase.this.length(), 0);
            }

            Splitter(long size, long index) {
                this.size = size;
                this.index = index;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.size)
                    return false;
                action.accept(SpanBase.this.get(this.index++));
                return true;
            }

            public Spliterator<E> trySplit() {
                long newIndex = this.index;
                long midPoint = (this.index + this.size) >>> 1;
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
