package dev.redio.span.buffer;

import java.nio.Buffer;
import java.util.Objects;

public abstract class AbstractBufferSpan<E, B extends Buffer> 
    implements BufferSpan<E,B> {

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
            return "BufferSpan[" + this.length() + "]";
    }
}
