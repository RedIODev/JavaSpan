package dev.redio.span.buffer;

import java.nio.FloatBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyFloatBufferSpan 
    extends ReadOnlyBufferSpan<Float,FloatBuffer> 
    implements Comparable<ReadOnlyFloatBufferSpan>
    permits FloatBufferSpan {

    public ReadOnlyFloatBufferSpan(FloatBuffer floatBuffer) {
        super(floatBuffer.duplicate());
    }

    @Override
    public Float get(int index) {
        return this.data.get(index);
    }

    public float getFloat(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyFloatBufferSpan duplicate() {
        return new ReadOnlyFloatBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyFloatBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyFloatBufferSpan slice(int start, int length) {
        return new ReadOnlyFloatBufferSpan(this.data.slice(start, length));
    }

    public float[] toFloatArray() {
        var floatArray = new float[this.length()];
        for (int i = 0; i < this.length(); i++)
            floatArray[i] = this.data.get(i);
        return floatArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int compareTo(ReadOnlyFloatBufferSpan o) {
        return Spans.compare(this, o);
    }

    @SuppressWarnings("null")
    @Override
    public boolean equals(Object obj) {
        if (!Spans.baseEquals(this, obj))
            return false;
        var span = (ReadOnlyFloatBufferSpan) obj;
        return Spans.arrayEquals(this.length(), span.length(), (i -> this.getFloat(i) == span.getFloat(i)));
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), (i -> (int)this.getFloat(i)));
    }
}
