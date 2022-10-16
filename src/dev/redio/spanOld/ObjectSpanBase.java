package dev.redio.spanOld;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.redio.utils.Streamable;

public interface ObjectSpanBase<E> extends Streamable<E>, SpanBase {
    
    E get(int index);

    default E[] toArray(IntFunction<E[]> generator) {
        Spans.checkSpanSize(this);
        E[] result = generator.apply(this.length());
        for (int i = 0; i < result.length; i++) 
            result[i] = this.get(i);
        return result;
    }

    default boolean contains(Object obj) {
        return this.indexOf(obj) >= 0;
    }

    default boolean containsAll(Iterable<?> i) {
        for (var object : i) 
            if (!this.contains(object))
                return false;
        return true;
    }

    default int indexOf(Object obj) {
        final int length = this.length();
        for (int i = 0; i < length; i++) 
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    default int lastIndexOf(Object obj) {
        
        for (int i = this.length(); i >= 0; i--) 
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    @Override
    default Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            int i = 0;
            @Override
            public boolean hasNext() {
                return this.i < ObjectSpanBase.this.length();
            }

            @Override
            public E next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();
                return ObjectSpanBase.this.get(i++);
            }
        }

        return new Iter();
    }

    @Override
    default Spliterator<E> spliterator() {
        class Splitter implements Spliterator<E> {
            private final int size;
            private int index = 0;

            Splitter() {
                this(ObjectSpanBase.this.length(), 0);
            }

            Splitter(int size, int index) {
                this.size = size;
                this.index = index;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.size)
                    return false;
                action.accept(ObjectSpanBase.this.get(this.index++));
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
