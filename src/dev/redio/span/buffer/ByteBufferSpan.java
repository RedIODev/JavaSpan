package dev.redio.span.buffer;

import java.nio.ByteBuffer;

import dev.redio.span.Span;

public final class ByteBufferSpan 
    extends ReadOnlyByteBufferSpan
    implements Span<Byte> {

    public ByteBufferSpan(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override
    public void set(int index, Byte value) {
        this.data.put(index,value);
    }

    public void setByte(int index, byte value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setByte(i, (byte)0);
    }

    @Override
    public void fill(Byte value) {
        for (int i = 0; i < this.length(); i++)
            this.setByte(i, value);
    }

    @Override
    public ByteBufferSpan duplicate() {
        return new ByteBufferSpan(this.data.duplicate());
    }

    @Override
    public ByteBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public ByteBufferSpan slice(int start, int length) {
        return new ByteBufferSpan(this.data.slice(start, length));
    }
}
