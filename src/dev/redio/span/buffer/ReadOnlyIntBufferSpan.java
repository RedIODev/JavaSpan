package dev.redio.span.buffer;

import java.nio.IntBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyIntBufferSpan 
    extends ReadOnlyBufferSpan<Integer,IntBuffer> 
    implements Comparable<ReadOnlyIntBufferSpan>
    permits IntBufferSpan {

    public ReadOnlyIntBufferSpan(IntBuffer intBuffer) {
        super(intBuffer.duplicate());
    }

    @Override
    public Integer get(int index) {
        return this.data.get(index);
    }

    public int getInt(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyIntBufferSpan duplicate() {
        return new ReadOnlyIntBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyIntBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyIntBufferSpan slice(int start, int length) {
        return new ReadOnlyIntBufferSpan(this.data.slice(start, length));
    }

    public int[] toIntArray() {
        var intArray = new int[this.length()];
        for (int i = 0; i < this.length(); i++)
            intArray[i] = this.data.get(i);
        return intArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyIntBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyIntBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getInt(i) == span.getInt(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getInt);
    }
}
