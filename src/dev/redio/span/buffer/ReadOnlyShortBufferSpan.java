package dev.redio.span.buffer;

import java.nio.ShortBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyShortBufferSpan 
    extends ReadOnlyBufferSpan<Short,ShortBuffer> 
    implements Comparable<ReadOnlyShortBufferSpan>
    permits ShortBufferSpan {

    public ReadOnlyShortBufferSpan(ShortBuffer shortBuffer) {
        super(shortBuffer.duplicate());
    }

    @Override
    public Short get(int index) {
        return this.data.get(index);
    }

    public short getShort(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyShortBufferSpan duplicate() {
        return new ReadOnlyShortBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyShortBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyShortBufferSpan slice(int start, int length) {
        return new ReadOnlyShortBufferSpan(this.data.slice(start, length));
    }

    public short[] toShortArray() {
        var shortArray = new short[this.length()];
        for (int i = 0; i < this.length(); i++)
            shortArray[i] = this.data.get(i);
        return shortArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyShortBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyShortBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getShort(i) == span.getShort(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getShort);
    }
}
