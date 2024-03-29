package dev.redio.span;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dev.redio.span.Spans.GenA;
import dev.redio.span.Spans.GenB;

public interface ReadOnlySpan<E> 
    extends Iterable<E> {

    static <T> ReadOnlySpan<T> of(T[] array) {
        return new ReadOnlyArraySpan<>(array);
    }

    static <T> ReadOnlySpan<T> of(T[] array, int start, int length) {
        return new ReadOnlyArraySpan<>(array, start, length);
    }

    static <T> ReadOnlySpan<T> of(List<T> list) {
        return new ReadOnlyListSpan<>(list);
    }

    static <T> ReadOnlySpan<T> of(List<T> list, int start, int length) {
        return new ReadOnlyListSpan<>(list, start, length);
    }

    static CharSequenceSpan of(CharSequence cs) {
        return new CharSequenceSpan(cs);
    }

    static CharSequenceSpan of(CharSequence cs, int start, int length) {
        return new CharSequenceSpan(cs, start, length);
    }

    static <S extends ReadOnlySpan<E>,E,T> S of(GenA<S,T> generator, T source) {
        return generator.generate(source);
    }

    static <S extends ReadOnlySpan<E>,E,T> S of(GenB<S,T> generator, T source, int start, int length) {
        return generator.generate(source, start, length);
    }


    
    E get(int index);

    int length();

    ReadOnlySpan<E> duplicate();

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

    default ReadOnlySpan<E> slice(int start) {
        return this.slice(start, this.length() - start);
    }

    ReadOnlySpan<E> slice(int start, int length);

    default Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return this.index != ReadOnlySpan.this.length();
            }

            @Override
            public E next() {
                if (this.index >= ReadOnlySpan.this.length())
                    throw new NoSuchElementException();
                return ReadOnlySpan.this.get(this.index++);
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
                action.accept(ReadOnlySpan.this.get(index));
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

    Object[] toArray();

    default E[] toArray(E[] array) {
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