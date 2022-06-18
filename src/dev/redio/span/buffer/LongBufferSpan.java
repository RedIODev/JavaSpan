package dev.redio.span.buffer;

import java.nio.LongBuffer;

import dev.redio.span.Span;

public final class LongBufferSpan 
    extends ReadOnlyLongBufferSpan
    implements Span<Long> {

    public LongBufferSpan(LongBuffer longBuffer) {
        super(longBuffer);
    }

    @Override
    public void set(int index, Long value) {
        this.data.put(index,value);
    }

    public void setLong(int index, long value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setLong(i, 0);
    }

    @Override
    public void fill(Long value) {
        for (int i = 0; i < this.length(); i++)
            this.setLong(i, value);
    }

    @Override
    public LongBufferSpan duplicate() {
        return new LongBufferSpan(this.data.duplicate());
    }

    @Override
    public LongBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public LongBufferSpan slice(int start, int length) {
        return new LongBufferSpan(this.data.slice(start, length));
    }
}
