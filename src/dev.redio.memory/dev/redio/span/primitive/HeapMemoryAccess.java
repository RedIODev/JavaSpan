package dev.redio.span.primitive;

import static dev.redio.span.primitive.Unsafe.UNSAFE;

import java.lang.reflect.Array;
import java.util.Objects;

record HeapMemoryAccess(Object array, long start, long length) implements MemoryAccess {

    public HeapMemoryAccess(Object array) {
        this(Objects.requireNonNull(array), UNSAFE.arrayBaseOffset(array.getClass()), validateArray(array));
    }

    @Override
    public HeapMemoryAccess slice(long start, long length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new HeapMemoryAccess(this.array, this.start + start, length);
    }

    @Override
    public byte getByte(long index) {
        return UNSAFE.getByte(this.array, Unsafe.calculateOffset(this, index, ByteSpan.SCALE));
    }

    @Override
    public char getChar(long index) {
        return UNSAFE.getChar(this.array, Unsafe.calculateOffset(this, index, CharSpan.SCALE));
    }

    @Override
    public short getShort(long index) {
        return UNSAFE.getShort(this.array, Unsafe.calculateOffset(this, index, ShortSpan.SCALE));
    }

    @Override
    public int getInt(long index) {
        return UNSAFE.getInt(this.array, Unsafe.calculateOffset(this, index, IntSpan.SCALE));
    }

    @Override
    public long getLong(long index) {
        return UNSAFE.getLong(this.array, Unsafe.calculateOffset(this, index, LongSpan.SCALE));
    }

    @Override
    public float getFloat(long index) {
        return UNSAFE.getFloat(this.array, Unsafe.calculateOffset(this, index, FloatSpan.SCALE));
    }

    @Override
    public double getDouble(long index) {
        return UNSAFE.getDouble(this.array, Unsafe.calculateOffset(this, index, DoubleSpan.SCALE));
    }

    @Override
    public void setByte(long index, byte value) {
        UNSAFE.putByte(this.array, Unsafe.calculateOffset(this, index, ByteSpan.SCALE), value);
    }

    @Override
    public void setChar(long index, char value) {
        UNSAFE.putChar(this.array, Unsafe.calculateOffset(this, index, CharSpan.SCALE), value);
    }

    @Override
    public void setShort(long index, short value) {
        UNSAFE.putShort(this.array, Unsafe.calculateOffset(this, index, ShortSpan.SCALE), value);
    }

    @Override
    public void setInt(long index, int value) {
        UNSAFE.putInt(this.array, Unsafe.calculateOffset(this, index, IntSpan.SCALE), value);
    }

    @Override
    public void setLong(long index, long value) {
        UNSAFE.putLong(this.array, Unsafe.calculateOffset(this, index, LongSpan.SCALE), value);
    }

    @Override
    public void setFloat(long index, float value) {
        UNSAFE.putFloat(this.array, Unsafe.calculateOffset(this, index, FloatSpan.SCALE), value);
    }

    @Override
    public void setDouble(long index, double value) {
        UNSAFE.putDouble(this.array, Unsafe.calculateOffset(this, index, DoubleSpan.SCALE), value);
    }

    private static long validateArray(Object array) {
        final int length = Array.getLength(array);
        final Class<?> clazz = array.getClass();
        final long bytesSize = UNSAFE.arrayIndexScale(clazz);
        if (bytesSize == 0)
            throw new IllegalArgumentException("The array is of narrow type.");
        return bytesSize * length;
    }
}
