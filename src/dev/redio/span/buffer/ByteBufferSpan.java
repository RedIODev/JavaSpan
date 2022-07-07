package dev.redio.span.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.function.Function;

import dev.redio.span.Span;
import dev.redio.span.Spans;

public final class ByteBufferSpan 
    extends AbstractBufferSpan<Byte,ByteBuffer>
    implements Comparable<ByteBufferSpan> {

    public ByteBufferSpan(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    public byte get(int index) {
        return this.data.get(index);
    }

    public void set(int index, byte value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        final int length = this.length();
        for (int i = 0; i < length; i++)
            this.set(i, (byte)0);
    }

    @Override
    public void fill(Byte value) {  //null is intepreted as 0
        if (value == null)
            value = 0;
        final int length = this.length();
        for (int i = 0; i < length; i++)
            this.set(i, value);
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
        return Spans.arrayCompare(this.length(), o.length(), (i -> this.get(i) - o.get(i)));
    }

    public <T,B extends Buffer,S extends BufferSpan<T,B>> S as(Function<ByteBuffer,S> generator) {
        return generator.apply(this.data.duplicate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ByteBufferSpan other)
            return Spans.arrayEquals(this.length(), other.length(), (i -> this.get(i) == other.get(i)));
        return false;
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::get);
    }


    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Byte))
            return -1;
        final int length = this.length();
        for (int i = 0; i < length; i++) 
            if(this.get(i) == (byte)(Byte)o)
                return i;
        return -1;
    }


    @Override
    public int lastIndexOf(Object o) {
        if (!(o instanceof Byte))
            return -1;
        for (int i = this.length()-1; i >= 0; i--) 
            if(this.get(i) == (byte)(Byte)o)
                return i;
        return -1;
    }


    @Override
    public Span<Byte> span() {
        return new Span.Builder<>(this.length(), this::get)
                .setFunction(this::set)
                .build();
    }


    @Override
    public ByteBuffer buffer() {
        return this.data.duplicate();
    }
}
