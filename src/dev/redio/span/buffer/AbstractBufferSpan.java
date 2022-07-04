package dev.redio.span.buffer;

import java.nio.Buffer;
import java.util.Objects;

import dev.redio.span.Span;

public abstract class AbstractBufferSpan<E, B extends Buffer> 
    implements Span<E> {

    protected final B data;

    protected AbstractBufferSpan(B buffer) {
        this.data = Objects.requireNonNull(buffer);
    }

    @Override
    public int length() {
        return data.remaining();
    }

    @Override
    public String toString() {
            return "Span[" + this.length() + "]";
    }
}
