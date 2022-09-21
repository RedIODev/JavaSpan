package dev.redio.span;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

interface SpanBase<E> extends Iterable<E> {

    int size();
    
    E getObj(int index);

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

    default boolean isEmpty() {
        return this.size() == 0;
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
                return SpanBase.this.size();
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
                if(a.length < SpanBase.this.size())
                    a = (T[])Array.newInstance(a.getClass().getComponentType(), SpanBase.this.size());
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
                // TODO Auto-generated method stub
                return false;
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
    }
}
