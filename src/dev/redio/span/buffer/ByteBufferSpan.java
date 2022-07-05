package dev.redio.span.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.function.Function;

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

    @Override
    public byte getByte(int index) {
        return this.data.get(index);
    }

    @Override
    public void set(int index, Byte value) {
        this.data.put(index,value);
    }

    @Override
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

    @Override
    public <T, U extends Buffer> BufferSpan<T, U> changeType(Function<ByteBuffer, U> converter) {
        
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ByteBufferSpan other)
            return Spans.arrayEquals(this.length(), other.length(), (i -> this.getByte(i) == other.getByte(i)));
        return false;
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getByte);
    }
}
