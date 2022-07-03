package dev.redio.span;

import java.util.Objects;

import dev.redio.span.function.GetFunction;
import dev.redio.span.function.SetFunction;

final class DynamicSpan<E> 
    extends AbstractSpan<E> {

    private final GetFunction<E> getFunction;
    private final SetFunction<E> setFunction;

    public DynamicSpan(Span.Builder<E> builder) {
        super(builder.start, builder.length);
        this.getFunction = builder.getFunction;
        this.setFunction = builder.setFunction;
    }

    private DynamicSpan(int start, int length,
                        GetFunction<E> getFunction,
                        SetFunction<E> setFunction) {
        super(start, length);
        this.getFunction = getFunction;
        this.setFunction = setFunction;
    }

    @Override
    public E get(int index) {
        return this.getFunction.get(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public void set(int index, E value) {
        if (this.setFunction == null)
            throw new UnsupportedOperationException();
        this.setFunction.set(this.start + Objects.checkIndex(index, this.length), value);
    }

    @Override
    public void fill(E value) {
        for (int i = 0; i < this.length; i++)
            this.set(i, value);
    }

    @Override
    public Span<E> slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new DynamicSpan<>(this.start + start, length, this.getFunction, this.setFunction);
    }
}
