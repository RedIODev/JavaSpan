package dev.redio.span.primitive;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Objects;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.Span;

import jdk.incubator.foreign.MemoryHandles;

public final class IntSpanVarHandle implements Span<Integer> {
    private final VarHandle handle;
    private final int size;
    private final int start;

    public IntSpanVarHandle(int[] data) {
        this(data, data.length);
    }

    public IntSpanVarHandle(int[] data, int size) {
        this(data, size, 0);
    }

    public IntSpanVarHandle(int[] data, int size, int start) {
        Objects.requireNonNull(data);
        VarHandle handle = MethodHandles.arrayElementVarHandle(data.getClass());
        this.handle = MemoryHandles.insertCoordinates(handle, 0, data);
        this.size = size;
        this.start = start;
    }

    private IntSpanVarHandle(VarHandle handle, int size, int start) {
        this.handle = handle;
        this.size = size;
        this.start = start;
    }

    int get(int index) {
        Objects.checkIndex(index, this.size);
        return (int) this.handle.get(index);
    }

    void set(int index, int value) {
        Objects.checkIndex(index, this.size);
        this.handle.set(index, value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Integer getObj(int index) {
        return this.get(index);
    }

    @Override
    public void setObj(int index, Integer value) {
        this.set(index, value);
    }

    @Override
    public IntSpanVarHandle slice(int start, int size) {
        return new IntSpanVarHandle(this.handle, size, start + this.start);
    }

    @Override
    public IntSpanVarHandle slice(int start) {
        return this.slice(start, this.size);
    }

    @Override
    public int indexOf(Object obj) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int lastIndexOf(Object obj) {
        // TODO Auto-generated method stub
        return 0;
    }

    

    @Override
    public ReadOnlySpan<Integer> readOnlySpan() {
        // TODO Auto-generated method stub
        return null;
    }
}
