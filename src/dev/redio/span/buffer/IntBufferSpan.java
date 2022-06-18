package dev.redio.span.buffer;

import java.nio.IntBuffer;

import dev.redio.span.Span;

public final class IntBufferSpan 
    extends ReadOnlyIntBufferSpan
    implements Span<Integer> {

    public IntBufferSpan(IntBuffer intBuffer) {
        super(intBuffer);
    }

    @Override
    public void set(int index, Integer value) {
        this.data.put(index,value);
    }

    public void setInt(int index, int value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setInt(i, 0);
    }

    @Override
    public void fill(Integer value) {
        for (int i = 0; i < this.length(); i++)
            this.setInt(i, value);
    }

    @Override
    public IntBufferSpan duplicate() {
        return new IntBufferSpan(this.data.duplicate());
    }

    @Override
    public IntBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public IntBufferSpan slice(int start, int length) {
        return new IntBufferSpan(this.data.slice(start, length));
    }
}
