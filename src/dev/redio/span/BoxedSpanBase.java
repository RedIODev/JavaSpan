package dev.redio.span;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface BoxedSpanBase<E> extends ObjectSpanBase<E>, OversizedSpanBase {

    E get(long index);

    PrimitiveSpanBase unboxed();

    @Override
    SpanBase subSequence(long start);

    @Override
    SpanBase subSequence(long start, long end);

    @Override
    default boolean contains(Object obj) {
        return (this.isOversized()) ? this.indexOfL(obj) >= 0 : ObjectSpanBase.super.contains(obj);
    }

    default long indexOfL(Object obj) {
        final long length = this.lengthL();
        for (long i = 0; i < length; i++)
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    default long lastIndexOfL(Object obj) {
       
        for (long i = this.lengthL(); i >= 0; i--)
            if (Objects.equals(this.get(i), obj))
                return i;
        return -1;
    }

    @Override
    default Iterator<E> iterator() {
        class IterL implements Iterator<E> {
            long l = 0;
            @Override
            public boolean hasNext() {
                return this.l < BoxedSpanBase.this.lengthL();
            }

            @Override
            public E next() {
                if (!this.hasNext())
                    throw new NoSuchElementException();
                return BoxedSpanBase.this.get(l++);
            }
        }
        
        return (this.isOversized()) ? new IterL() : ObjectSpanBase.super.iterator();
    }

    @Override
    default Spliterator<E> spliterator() {
        class SplitterL implements Spliterator<E> {
            private final long length;
            private long index = 0;

            SplitterL() {
                this(BoxedSpanBase.this.length(), 0);
            }

            SplitterL(long size, long index) {
                this.length = size;
                this.index = index;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.length)
                    return false;
                action.accept(BoxedSpanBase.this.get(this.index++));
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
                return ORDERED | SIZED | SUBSIZED;
            }
        }

        return (this.isOversized()) ? new SplitterL() : ObjectSpanBase.super.spliterator();
    }
}
