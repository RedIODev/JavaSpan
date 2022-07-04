package dev.redio.span;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

final class UnmodifiableSpan<E>
        implements Span<E> {

    private final Span<? extends E> data;

    public UnmodifiableSpan(Span<? extends E> span) {
        this.data = Objects.requireNonNull(span);
    }

    @Override
    public void fill(E value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int index, E value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        data.forEach(action);
    }

    @Override
    public int length() {
        return data.length();
    }

    @Override
    public E get(int index) {
        return data.get(index);
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            private final Iterator<? extends E> i = UnmodifiableSpan.this.data.iterator();

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public E next() {
                return i.next();
            }
            
            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                i.forEachRemaining(action);
            }
        }
        return new Iter();
    }

    @Override
    public Span<E> slice(int start) {
        return new UnmodifiableSpan<>(data.slice(start));
    }

    @Override
    public Span<E> slice(int start, int length) {
        return new UnmodifiableSpan<>(data.slice(start, length));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Spliterator<E> spliterator() {
        return (Spliterator<E>) data.spliterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<E> stream() {
        return (Stream<E>) data.stream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<E> parallelStream() {
        return (Stream<E>) data.parallelStream();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return data.toArray(array);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return data.toArray(generator);
    }

    @Override
    public boolean equals(Object obj) {
        return data.equals(obj);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}