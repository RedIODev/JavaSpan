package dev.redio.span.buffer;

import java.nio.DoubleBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyDoubleBufferSpan 
    extends ReadOnlyBufferSpan<Double,DoubleBuffer> 
    implements Comparable<ReadOnlyDoubleBufferSpan>
    permits DoubleBufferSpan {

    public ReadOnlyDoubleBufferSpan(DoubleBuffer doubleBuffer) {
        super(doubleBuffer.duplicate());
    }

    @Override
    public Double get(int index) {
        return this.data.get(index);
    }

    public double getDouble(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyDoubleBufferSpan duplicate() {
        return new ReadOnlyDoubleBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyDoubleBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyDoubleBufferSpan slice(int start, int length) {
        return new ReadOnlyDoubleBufferSpan(this.data.slice(start, length));
    }

    public double[] toDoubleArray() {
        var doubleArray = new double[this.length()];
        for (int i = 0; i < this.length(); i++)
            doubleArray[i] = this.data.get(i);
        return doubleArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyDoubleBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyDoubleBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.get(i) == span.get(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), (i -> (int)this.getDouble(i)));
    }
}
