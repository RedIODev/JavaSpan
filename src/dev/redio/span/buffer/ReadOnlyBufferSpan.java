package dev.redio.span.buffer;

import java.nio.Buffer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.Span;

public abstract class ReadOnlyBufferSpan<T, B extends Buffer> 
    implements ReadOnlySpan<T> {

    protected final B data;

    protected ReadOnlyBufferSpan(B buffer) {
        this.data = Objects.requireNonNull(buffer);
    }

    @Override
    public int length() {
        return data.remaining();
    }

    @Override
    public Iterator<T> iterator() {
        class Iter implements Iterator<T> {
            int index = 0;

            @Override
            public boolean hasNext() {
                return this.index != ReadOnlyBufferSpan.this.length();
            }

            @Override
            public T next() {
                if (index >= ReadOnlyBufferSpan.this.length())
                    throw new NoSuchElementException();
                return ReadOnlyBufferSpan.this.get(index);
            }
        }
        return new Iter();
    }

    @Override
    public Object[] toArray() {
        var array = new Object[this.length()];
        for (int i = 0; i < this.length(); i++)
            array[i] = this.get(i);
        return array;
    }

    @Override
    public String toString() {
        if (this instanceof Span)
            return "Span[" + this.length() + "]";
        return "ReadOnlySpan[" + this.length() + "]";
    }

    
}
