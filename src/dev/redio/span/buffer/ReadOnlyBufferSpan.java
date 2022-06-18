package dev.redio.span.buffer;

import java.nio.Buffer;
import java.util.Objects;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.Span;

public abstract class ReadOnlyBufferSpan<E, B extends Buffer> 
    implements ReadOnlySpan<E> {

    protected final B data;

    protected ReadOnlyBufferSpan(B buffer) {
        this.data = Objects.requireNonNull(buffer);
    }

    @Override
    public int length() {
        return data.remaining();
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
