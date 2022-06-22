package dev.redio.span;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dev.redio.span.Spans.IntBiConsumer;

public interface Span<E> 
    extends Iterable<E> {

    static <T> Span<T> of(T[] array) {
        return new ArraySpan<>(array);
    }

    static <T> Span<T> of(T[] array, int start, int end) {
        return new ArraySpan<>(array, start, end);
    }

    static <T> Span<T> of(List<T> list) {
        return new ListSpan<>(list);
    }

    static <T> Span<T> of(List<T> list, int start, int length) {
        return new ListSpan<>(list, start, length);
    }

    public static class Builder<E> {
        int start = 0;
        final int length;
        final IntFunction<E> getFunction;
        IntBiConsumer<E> setFunction;

        public Builder(int length,
                       IntFunction<E> getFunction) {
            this.length = length;
            this.getFunction = getFunction;
        }

        public Builder<E> start(int start) {
            this.start = start;
            return this;
        }

        public Builder<E> setFunction(IntBiConsumer<E> setFunction) {
            this.setFunction = setFunction;
            return this;
        }

        public Span<E> build() {
            this.validateBuilder();
            return new DynamicSpan<>(this);
        }

        private void validateBuilder() {
            if (this.start < 0 || this.length < 0)
                throw new IllegalArgumentException();
            Objects.requireNonNull(this.getFunction);
        }
    }

    int length();

    E get(int index);
    
    void set(int index, E value);

    default void clear() {
        fill(null);
    }

    void fill(E value);

    default boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    default boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    default int indexOf(Object o) {
        for (int i = 0; i < this.length(); i++) 
            if (Objects.equals(this.get(i), o))
                return i;
        return -1;
    }

    default int lastIndexOf(Object o) {
        for (int i = this.length() - 1; i >= 0; i--) 
            if (Objects.equals(this.get(i), o))
                return i;
        return -1;
    }

    Span<E> duplicate();

    default boolean isEmpty() {
        return this.length() == 0;
    }

    default void copyTo(Span<E> destination) {
        if (!this.tryCopyTo(destination))
            throw new IllegalArgumentException("The destination Span is null or shorter that the source Span.");
    }

    default boolean tryCopyTo(Span<E> destination) {
        if (destination == null || this.length() > destination.length())
            return false;
        for (int i = 0; i < this.length(); i++)
            destination.set(i, this.get(i));
        return true;
    }

    default Span<E> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    Span<E> slice(int start, int length);

    default Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return this.index != Span.this.length();
            }

            @Override
            public E next() {
                if (this.index >= Span.this.length())
                    throw new NoSuchElementException();
                return Span.this.get(this.index++);
            }
        }
        return new Iter();
    }

    @Override
    default Spliterator<E> spliterator() {
        class Spliter implements Spliterator<E> {
            private int index;
            private final int length;
        
            Spliter(int index, int length) {
                this.index = index;
                this.length = length;
            }
        
            @Override
            public boolean tryAdvance(Consumer<? super E> action) {
                if (index >= length) 
                    return false;
                action.accept(Span.this.get(index));
                index++;
                return true;
            }
        
            @Override
            public Spliterator<E> trySplit() {
                int newIndex = this.index;
                int midPoint = (this.index + this.length) >>> 1;
                if (index >= midPoint)
                    return null;
                this.index = midPoint;
                return new Spliter(newIndex, midPoint);
            }
        
            @Override
            public long estimateSize() {
                return (this.length - this.index);
            }
        
            @Override
            public int characteristics() {
                return ORDERED | SIZED | SUBSIZED;
            }
        }
        return new Spliter(0, this.length());
    }

    default Stream<E> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }

    default Object[] toArray() {
        Object[] array = new Object[this.length()];
        for (int i = 0; i < this.length(); i++) 
            array[i] = this.get(i);
        return array;
    }

    default <T> T[] toArray(T[] array) {
        return Spans.toArray(this, array);
    }

    default E[] toArray(IntFunction<E[]> generator) {
        return toArray(generator.apply(0));
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
