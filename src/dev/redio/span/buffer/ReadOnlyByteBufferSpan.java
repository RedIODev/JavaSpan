package dev.redio.span.buffer;

import java.nio.ByteBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyByteBufferSpan 
    extends ReadOnlyBufferSpan<Byte,ByteBuffer> 
    implements Comparable<ReadOnlyByteBufferSpan>
    permits ByteBufferSpan {

    public ReadOnlyByteBufferSpan(ByteBuffer byteBuffer) {
        super(byteBuffer.duplicate());
    }

    @Override
    public Byte get(int index) {
        return this.data.get(index);
    }

    public byte getByte(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyByteBufferSpan duplicate() {
        return new ReadOnlyByteBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyByteBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyByteBufferSpan slice(int start, int length) {
        return new ReadOnlyByteBufferSpan(this.data.slice(start, length));
    }

    public byte[] toByteArray() {
        var byteArray = new byte[this.length()];
        for (int i = 0; i < this.length(); i++)
            byteArray[i] = this.data.get(i);
        return byteArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyByteBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyByteBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getByte(i) == span.getByte(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getByte);
    }
}
