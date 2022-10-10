package dev.redio.span.primitive;

import java.util.Objects;

import dev.redio.span.ReadOnlySpanOld;
import dev.redio.span.SpanOld;
import dev.redio.span.SpanBaseOld;

public final class IntSpanArray implements SpanOld<Integer> {

    private final int[] data;
    private final int length;
    private final int start;

    public IntSpanArray(int[] data) {
        this(data, data.length);
    }

    public IntSpanArray(int[] data, int start) {
        this(data, start, data.length);
    }

    public IntSpanArray(int[] data, int start, int length) {
        this.data = Objects.requireNonNull(data);
        this.length = length;
        this.start = start;
    }

    @Override
    public int length() {
        return this.length;
    }

    public long lengthL() {
        return this.length;
    }

    public int get(int index) {
        Objects.checkIndex(index, this.length);
        return this.data[index + this.start];
    }

    public void set(int index, int value) {
        Objects.checkIndex(index, this.length);
        this.data[index + this.start] = value;
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
    public Integer getObj(long index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setObj(long index, Integer value) {
        
    }

    @Override
    public IntSpanArray slice(int start, int size) {
        return new IntSpanArray(data, this.length, start + this.start);
    }

    @Override
    public IntSpanArray slice(int start) {
        return this.slice(start, this.length);
    }

    @Override
    public SpanBaseOld<Integer> slice(long start, long size) {
        return this.slice((int)start, (int)size);
    }

    @Override
    public SpanBaseOld<Integer> slice(long start) {
        return this.slice((int)start);
    }

    @Override
    public int indexOf(Object obj) {
        if (obj instanceof Integer integer) 
            for (int i = 0; i < this.length; i++) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object obj) {
        if (obj instanceof Integer integer) 
            for (int i = this.length - 1; i >= 0; i--) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    @Override
    public long indexOfL(Object obj) {
        return indexOf(obj);
    }

    @Override
    public long lastIndexOfL(Object obj) {
        return lastIndexOf(obj);
    }
    

    @Override
    public ReadOnlySpanOld<Integer> readOnlySpan() {
        // TODO Auto-generated method stub
        return null;
    }
    
    int[] toArray() {
        int[] array = new int[this.length];
        System.arraycopy(this.data, start, array, 0, this.length);
        return array;
    }
}
