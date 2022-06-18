package dev.redio.span.buffer;

import java.nio.LongBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyLongBufferSpan 
    extends ReadOnlyBufferSpan<Long,LongBuffer> 
    implements Comparable<ReadOnlyLongBufferSpan>
    permits LongBufferSpan {

    public ReadOnlyLongBufferSpan(LongBuffer longBuffer) {
        super(longBuffer.duplicate());
    }

    @Override
    public Long get(int index) {
        return this.data.get(index);
    }

    public long getLong(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyLongBufferSpan duplicate() {
        return new ReadOnlyLongBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyLongBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyLongBufferSpan slice(int start, int length) {
        return new ReadOnlyLongBufferSpan(this.data.slice(start, length));
    }

    public long[] toLongArray() {
        var longArray = new long[this.length()];
        for (int i = 0; i < this.length(); i++)
            longArray[i] = this.data.get(i);
        return longArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyLongBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyLongBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getLong(i) == span.getLong(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), (i -> (int)this.getLong(i)));
    }
}
