package dev.redio.span;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class ReadOnlyBaseSpan<T> 
    implements ReadOnlySpan<T> {
    
    protected final int start;
    protected final int length;
    private int hash;
    private boolean isHashCached;

    protected ReadOnlyBaseSpan(int start, int length) {
        this.start = start;
        this.length = length;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public Iterator<T> iterator() {
        class Iter implements Iterator<T> {
            
            private int index = ReadOnlyBaseSpan.this.start;

            @Override
            public boolean hasNext() {
                return this.index != ReadOnlyBaseSpan.this.length;
            }

            @Override
            public T next() {
                if (this.index >= ReadOnlyBaseSpan.this.length)
                    throw new NoSuchElementException();
                return ReadOnlyBaseSpan.this.get(this.index++);
            }
        }
        return new Iter();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ReadOnlyBaseSpan))
            return false;
        ReadOnlyBaseSpan<?> span = (ReadOnlyBaseSpan<?>) obj;
        if (this.length != span.length)
            return false;
        for (int i = 0; i < this.length; i++)
            if (!Objects.equals(this.get(i), span.get(i)))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (isHashCached) 
            return hash;
        int h = 0;
        for (int i = 0; i < this.length; i++)
            h = 31 * h + nullableHashCode(this.get(i));
        this.hash = h;
        this.isHashCached = true;
        return h;
    }

    @Override
    public String toString() {
        if (this instanceof Span)
            return "Span[" + this.length + "]";
        return "ReadOnlySpan[" + this.length + "]";
    }

    private int nullableHashCode(T obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }
}