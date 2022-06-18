package dev.redio.span.buffer;

import java.nio.ShortBuffer;

import dev.redio.span.Span;

public final class ShortBufferSpan 
    extends ReadOnlyShortBufferSpan
    implements Span<Short> {

    public ShortBufferSpan(ShortBuffer shortBuffer) {
        super(shortBuffer);
    }

    @Override
    public void set(int index, Short value) {
        this.data.put(index,value);
    }

    public void setShort(int index, short value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setShort(i, (short)0);
    }

    @Override
    public void fill(Short value) {
        for (int i = 0; i < this.length(); i++)
            this.setShort(i, value);
    }

    @Override
    public ShortBufferSpan duplicate() {
        return new ShortBufferSpan(this.data.duplicate());
    }

    @Override
    public ShortBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public ShortBufferSpan slice(int start, int length) {
        return new ShortBufferSpan(this.data.slice(start, length));
    }
}
