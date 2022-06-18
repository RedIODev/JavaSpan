package dev.redio.span.buffer;

import java.nio.FloatBuffer;

import dev.redio.span.Span;

public final class FloatBufferSpan 
    extends ReadOnlyFloatBufferSpan
    implements Span<Float> {

    public FloatBufferSpan(FloatBuffer floatBuffer) {
        super(floatBuffer);
    }

    @Override
    public void set(int index, Float value) {
        this.data.put(index,value);
    }

    public void setFloat(int index, float value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setFloat(i, 0);
    }

    @Override
    public void fill(Float value) {
        for (int i = 0; i < this.length(); i++)
            this.setFloat(i, value);
    }

    @Override
    public FloatBufferSpan duplicate() {
        return new FloatBufferSpan(this.data.duplicate());
    }

    @Override
    public FloatBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public FloatBufferSpan slice(int start, int length) {
        return new FloatBufferSpan(this.data.slice(start, length));
    }
}
