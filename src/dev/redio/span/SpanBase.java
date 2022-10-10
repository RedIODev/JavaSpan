package dev.redio.span;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public interface SpanBase<E> extends Iterable<E> {
    
    int length();

    long lengthL();

    E get(int index);

    E get(long index);

    default SpanBase<E> subSequence(int start) {
        return this.subSequence(start, this.length());
    }

    SpanBase<E> subSequence(int start, int end);

    default SpanBase<E> subSequence(long start) {
        return this.subSequence(start, this.lengthL());
    }

    SpanBase<E> subSequence(long start, long end);

    default boolean isEmpty() {
        return this.lengthL() == 0;
    }

    default boolean isOversized() {
        return this.length() == -1;
    }

    default E[] toArray(IntFunction<E[]> generator) {
        Spans.checkSpanSize(this);
        E[] result = generator.apply(this.length());
        for (int i = 0; i < result.length; i++) 
            result[i] = this.get(i);
        return result;
    }

    @Override
    default Iterator<E> iterator() {
        class Iter implements Iterator<E> {
            int i = 0;
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

        class IterL implements Iterator<E> {
            long l = 0;
            @Override
            public boolean hasNext() {
                return this.l < SpanBase.this.lengthL();
            }

            @Override
            public E next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();
                return SpanBase.this.get(l++);
            }
        }
        return (this.isOversized()) ? new IterL() : new Iter();
    }

    @Override
    default Spliterator<E> spliterator() {
        class Splitter implements Spliterator<E> {
            private final int size;
            private int index = 0;

            Splitter() {
                this(SpanBase.this.length(), 0);
            }

            Splitter(int size, int index) {
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

        class SplitterL implements Spliterator<E> {
            private final long length;
            private long index = 0;

            SplitterL() {
                this(SpanBase.this.length(), 0);
            }

            SplitterL(long size, long index) {
                this.length = size;
                this.index = index;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.length)
                    return false;
                action.accept(SpanBase.this.get(this.index++));
                return true;
            }

            public Spliterator<E> trySplit() {
                long newIndex = this.index;
                long midPoint = (this.index + this.length) >>> 1;
                if (this.index >= midPoint)
                    return null;
                this.index = midPoint;
                return new SplitterL(newIndex, midPoint);
            }

            public long estimateSize() {
                return (this.length - this.index);
            }

            public int characteristics() {
                return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
            }
        }

        return (this.isOversized()) ? new SplitterL() : new Splitter();
    }
}
