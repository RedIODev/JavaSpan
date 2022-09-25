package dev.redio.span;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SpanBase<E> extends Iterable<E> {

    int length();

    long lengthL();
    
    E getObj(int index);

    E getObj(long index);

    SpanBase<E> slice(int start, int size);

    SpanBase<E> slice(int start);

    SpanBase<E> slice(long start, long size);

    SpanBase<E> slice(long start);

    default boolean contains(Object obj) {
        return this.indexOf(obj) >= 0;
    }

    default boolean containsAll(Iterable<?> i) {
        for (Object object : i) {
            if (this.contains(object))
                return true;
        }
        return false;
    }

    int indexOf(Object obj);

    int lastIndexOf(Object obj);

    long indexOfL(Object obj);

    long lastIndexOfL(Object obj);

    default boolean isEmpty() {
        return this.lengthL() == 0;
    }

    default boolean isOverSized() {
        return this.length() == -1;
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
                return SpanBase.this.length();
            }

            @Override
            public boolean isEmpty() {
                return SpanBase.this.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return SpanBase.this.contains(o);
            }

            @Override
            public Iterator<E> iterator() {
                return SpanBase.this.iterator();
            }

            @Override
            public Object[] toArray() {
                return Spans.toObjArray(Object[]::new, SpanBase.this);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T[] toArray(T[] a) {
                if(a.length < SpanBase.this.length())
                    a = (T[])Array.newInstance(a.getClass().getComponentType(), SpanBase.this.length());
                return Spans.toObjArray(a, SpanBase.this);
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
                return SpanBase.this.containsAll(c);
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
                throw new UnsupportedOperationException();
            }
            
        }
        Spans.checkSpanSize(this);
        return new Collect();
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
                return SpanBase.this.getObj(i++);
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
                return SpanBase.this.getObj(l++);
            }
        }
        return (this.isOverSized()) ? new IterL() : new Iter();
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
                action.accept(SpanBase.this.getObj(this.index++));
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
                return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
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
                action.accept(SpanBase.this.getObj(this.index++));
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

        return (this.isOverSized()) ? new SplitterL() : new Splitter();
    }
}
