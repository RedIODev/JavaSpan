package dev.redio.span.buffer;

import java.nio.DoubleBuffer;

import dev.redio.span.Span;

public final class DoubleBufferSpan 
    extends ReadOnlyDoubleBufferSpan
    implements Span<Double> {

    public DoubleBufferSpan(DoubleBuffer doubleBuffer) {
        super(doubleBuffer);
    }

    @Override
    public void set(int index, Double value) {
        this.data.put(index,value);
    }

    public void setDouble(int index, double value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setDouble(i, 0);
    }

    @Override
    public void fill(Double value) {
        for (int i = 0; i < this.length(); i++)
            this.setDouble(i, value);
    }

    @Override
    public DoubleBufferSpan duplicate() {
        return new DoubleBufferSpan(this.data.duplicate());
    }

    @Override
    public DoubleBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public DoubleBufferSpan slice(int start, int length) {
        return new DoubleBufferSpan(this.data.slice(start, length));
    }
}