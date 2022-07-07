package dev.redio.span;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
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

import dev.redio.span.function.GetFunction;
import dev.redio.span.function.SetFunction;

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

    static Span<Byte> of(ByteBuffer byteBuffer) {
        return new Builder<>(byteBuffer.remaining(),byteBuffer::get)
                .setFunction(byteBuffer::put)
                .build();
    }

    static Span<Character> of(CharBuffer charBuffer) {
        return new Builder<>(charBuffer.remaining(), charBuffer::get)
                .setFunction(charBuffer::put)
                .build();
    }

    static Span<Double> of(DoubleBuffer doubleBuffer) {
        return new Builder<>(doubleBuffer.remaining(), doubleBuffer::get)
                .setFunction(doubleBuffer::put)
                .build();
    }

    static Span<Float> of(FloatBuffer floatBuffer) {
        return new Builder<>(floatBuffer.remaining(),floatBuffer::get)
                .setFunction(floatBuffer::put)
                .build();
    }

    static Span<Integer> of(IntBuffer intBuffer) {
        return new Builder<>(intBuffer.remaining(),intBuffer::get)
                .setFunction(intBuffer::put)
                .build();
    }

    static Span<Long> of(LongBuffer longBuffer) {
        return new Builder<>(longBuffer.remaining(),longBuffer::get)
                .setFunction(longBuffer::put)
                .build();
    }

    static Span<Short> of(ShortBuffer shortBuffer) {
        return new Builder<>(shortBuffer.remaining(),shortBuffer::get)
                .setFunction(shortBuffer::put)
                .build();
    }

    public static class Builder<E> {
        int start = 0;
        final int length;
        final GetFunction<E> getFunction;
        SetFunction<E> setFunction;

        public Builder(int length, GetFunction<E> getFunction) {
            this.length = length;
            this.getFunction = getFunction;
        }

        public Builder<E> start(int start) {
            this.start = start;
            return this;
        }

        public Builder<E> setFunction(SetFunction<E> setFunction) {
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
        this.fill(null);
    }

    default void fill(E value) {
        final int length = this.length();
        for (int i = 0; i < length; i++) {
            this.set(i, value);
        }
    }

    default boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    default boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!this.contains(e))
                return false;
        return true;
    }

    default boolean copyFrom(Span<? extends E> span) {
        if (span == null)
            return false;
        final int length = span.length();
        if (length > this.length())
            throw new IllegalStateException();
        for (int i = 0; i < length; i++)
            this.set(i, span.get(i));
        return true;
    }

    default int indexOf(Object o) {
        final int length = this.length();
        for (int i = 0; i < length; i++) 
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

    default boolean isEmpty() {
        return this.length() == 0;
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

    default Collection<E> collection() {
        class Collect implements Collection<E> {

            @Override
            public int size() {
                return Span.this.length();
            }

            @Override
            public boolean isEmpty() {
                return Span.this.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return Span.this.contains(o);
            }

            @Override
            public Iterator<E> iterator() {
                return Span.this.iterator();
            }

            @Override
            public Object[] toArray() {
                return Span.this.toArray();
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return Span.this.toArray(a);
            }

            @Override
            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return Span.this.containsAll(c);
            }

            @Override
            public boolean addAll(Collection<? extends E> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                Span.this.clear();
            }

        }
        return new Collect();
    }

    default Object[] toArray() {
        final int length = this.length();
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++) 
            array[i] = this.get(i);
        return array;
    }

    default <T> T[] toArray(T[] array) {
        return Spans.toArray(this, array);
    }

    @SuppressWarnings("unchecked")
    default <T> T[] toArray(IntFunction<T[]> generator) {
        return Spans.toArray((IntFunction<E>)this::get, this.length(), generator,
                ((array, index, getFunction) -> array[index] = (T)getFunction.apply(index)));
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
