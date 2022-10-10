package dev.redio.span.primitive;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;


import dev.redio.span.ReadOnlySpanOld;
import dev.redio.span.SpanOld;
import dev.redio.span.Spans;

public final class IntSpanMemorySegment implements SpanOld<Integer> {
    private static final ValueLayout.OfInt LAYOUT = ValueLayout.JAVA_INT;
    private static final long JAVA_INT_BYTE_SIZE = LAYOUT.byteSize();
    private final MemorySegment memory;
    private final long length;

    public IntSpanMemorySegment(int[] data) {
        this(data, data.length);
    }

    public IntSpanMemorySegment(int[] data, int start) {
        this(data, start, data.length);
    }

    public IntSpanMemorySegment(int[] data, int start, int length) {
        Objects.requireNonNull(data);
        this.memory = MemorySegment.ofArray(data).asSlice(start * JAVA_INT_BYTE_SIZE, length * JAVA_INT_BYTE_SIZE);
        this.length = length;
    }

    public IntSpanMemorySegment(MemorySegment memory) {
        this(memory, 0);
    }

    public IntSpanMemorySegment(MemorySegment memory, long start) {
        this(memory, start, memory.byteSize() / JAVA_INT_BYTE_SIZE);
    }

    public IntSpanMemorySegment(MemorySegment memory, long start, long length) {
        this.memory = Objects.requireNonNull(memory.asSlice(start * JAVA_INT_BYTE_SIZE, length * JAVA_INT_BYTE_SIZE));
        this.length = length;
    } 

    @Override
    public int length() {
        return (this.isOverSized()) ? (int)this.length : -1;
    }

    @Override
    public long lengthL() {
        return this.length;
    }

    int get(long index) {
        Spans.checkSpanSize(this);
        Objects.checkIndex(index, this.length);
        return this.memory.getAtIndex(LAYOUT, index);
    }

    void set(long index, int value) {
        Spans.checkSpanSize(this);
        Objects.checkIndex(index, this.length);
        this.memory.setAtIndex(LAYOUT, index, value);
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
        return this.get(index);
    }

    @Override
    public void setObj(long index, Integer value) {
        this.set(index, value);
    }

    @Override
    public IntSpanMemorySegment slice(int start, int length) {
        return this.slice((long)start, (long)length);
    }

    @Override
    public IntSpanMemorySegment slice(int start) {
        return this.slice((long)start);
    }

    @Override
    public IntSpanMemorySegment slice(long start, long size) {
        return new IntSpanMemorySegment(this.memory, start, length);
    }

    @Override
    public IntSpanMemorySegment slice(long start) {
        return this.slice(start, (this.length - start));
    }

    @Override
    public int indexOf(Object obj) {
        Spans.checkSpanSize(this);
        if (obj instanceof Integer integer)
            for (int i = 0; i < this.length; i++) 
                if(this.get(i) == integer)
                    return i;
        return -1; 
    }

    @Override
    public int lastIndexOf(Object obj) {
        Spans.checkSpanSize(this);
        if (obj instanceof Integer integer) 
            for (int i = (int)this.length - 1; i >= 0; i--) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    @Override
    public long indexOfL(Object obj) {
        if (obj instanceof Integer integer)
            for (long i = 0; i < this.length; i++) 
                if(this.get(i) == integer)
                    return i;
        return -1; 
    }

    @Override
    public long lastIndexOfL(Object obj) {
        if (obj instanceof Integer integer) 
            for (int i = (int)this.length - 1; i >= 0; i--) 
                if (this.get(i) == integer)
                    return i;
        return -1;
    }

    

    @Override
    public ReadOnlySpanOld<Integer> readOnlySpan() {
        // TODO Auto-generated method stub
        return null;
    }

    int[] toArray() {
        Spans.checkSpanSize(this);
        int[] array = new int[(int)this.length];
        for (int i = 0; i < this.length; i++) 
            array[i] = this.get(i);
        return array;
    }
}
