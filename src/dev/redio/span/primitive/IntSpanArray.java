package dev.redio.span.primitive;

import java.util.Objects;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.Span;

public final class IntSpanArray implements Span<Integer> {

    private final int[] data;
    private final int size;
    private final int start;

    public IntSpanArray(int[] data) {
        this(data, data.length);
    }

    public IntSpanArray(int[] data, int size) {
        this(data, size, 0);
    }

    public IntSpanArray(int[] data, int size, int start) {
        this.data = Objects.requireNonNull(data);
        this.size = size;
        this.start = start;
    }

    public int get(int index) {
        Objects.checkIndex(index, this.size);
        return this.data[index + this.start];
    }

    public void set(int index, int value) {
        Objects.checkIndex(index, this.size);
        this.data[index + this.start] = value;
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
    public IntSpanArray slice(int start, int size) {
        return new IntSpanArray(data, this.size, start + this.start);
    }

    @Override
    public IntSpanArray slice(int start) {
        return this.slice(start, this.size);
    }

    @Override
    public int indexOf(Object obj) {
        if (obj instanceof Integer integer) 
            for (int i = 0; i < this.size; i++) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object obj) {
        if (obj instanceof Integer integer) 
            for (int i = this.size - 1; i >= 0; i--) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    

    @Override
    public ReadOnlySpan<Integer> readOnlySpan() {
        // TODO Auto-generated method stub
        return null;
    }
    
    int[] toArray() {
        int[] array = new int[this.size];
        System.arraycopy(this.data, start, array, 0, this.size);
        return array;
    }


}
