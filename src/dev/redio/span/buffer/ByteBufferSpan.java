package dev.redio.span.buffer;

import java.nio.ByteBuffer;

import dev.redio.span.Spans;

public final class ByteBufferSpan 
    extends AbstractBufferSpan<Byte,ByteBuffer>
    implements Comparable<ByteBufferSpan> {

    public ByteBufferSpan(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override
    public Byte get(int index) {
        return this.data.get(index);
    }

    public byte getByte(int index) {
        return this.data.get(index);
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
    public ByteBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public ByteBufferSpan slice(int start, int length) {
        return new ByteBufferSpan(this.data.slice(start, length));
    }

    @Override
    public int compareTo(ByteBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ByteBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getByte(i) == span.getByte(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getByte);
    }
}
