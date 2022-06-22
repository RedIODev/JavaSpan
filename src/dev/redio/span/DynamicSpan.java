package dev.redio.span;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.redio.span.Spans.IntBiConsumer;

class DynamicSpan<E> extends AbstractSpan<E> {

    private final IntFunction<E> getFunction;
    private final IntBiConsumer<E> setFunction;

    public DynamicSpan(Span.Builder<E> builder) {
        super(builder.start, builder.length);
        this.getFunction = builder.getFunction;
        this.setFunction = builder.setFunction;
    }

    private DynamicSpan(int start, int length,
                        IntFunction<E> getFunction,
                        IntBiConsumer<E> setFunction) {
        super(start, length);
        this.getFunction = getFunction;
        this.setFunction = setFunction;
    }

    @Override
    public E get(int index) {
        return this.getFunction.apply(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public void set(int index, E value) {
        if (this.setFunction == null)
            throw new UnsupportedOperationException();
        this.setFunction.accept(this.start + Objects.checkIndex(index, this.length), value);
    }

    @Override
    public void fill(E value) {
        for (int i = 0; i < this.length; i++)
            this.set(i, value);
    }

    @Override
    public Span<E> duplicate() {
        return new DynamicSpan<>(this.start, this.length, this.getFunction, this.setFunction);
    }

    @Override
    public Span<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new DynamicSpan<>(this.start + start, length, this.getFunction, this.setFunction);
    }




    
}
