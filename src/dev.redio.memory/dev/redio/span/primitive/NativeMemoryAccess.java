package dev.redio.span.primitive;

import static dev.redio.span.primitive.Unsafe.UNSAFE;

import java.util.Objects;


record NativeMemoryAccess(long start, long length) implements MemoryAccess {

    @Override
    public NativeMemoryAccess slice(long start, long length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new NativeMemoryAccess(this.start + start, length);
    }

    public byte getByte(long index) {
        return UNSAFE.getByte(Unsafe.calculateOffset(this, index, ByteSpan.SCALE));
    }

    public char getChar(long index) {
        return UNSAFE.getChar(Unsafe.calculateOffset(this, index, CharSpan.SCALE));
    }

    public short getShort(long index) {
        return UNSAFE.getShort(Unsafe.calculateOffset(this, index, LongSpan.SCALE));
    }

    public int getInt(long index) {
        return UNSAFE.getInt(Unsafe.calculateOffset(this, index, IntSpan.SCALE));
    }

    public long getLong(long index) {
        return UNSAFE.getLong(Unsafe.calculateOffset(this, index, LongSpan.SCALE));
    }

    public float getFloat(long index) {
        return UNSAFE.getFloat(Unsafe.calculateOffset(this, index, FloatSpan.SCALE));
    }

    public double getDouble(long index) {
        return UNSAFE.getDouble(Unsafe.calculateOffset(this, index, DoubleSpan.SCALE));
    }

    public void setByte(long index, byte value) {
        UNSAFE.putByte(Unsafe.calculateOffset(this, index, ByteSpan.SCALE), value);
    }

    public void setChar(long index, char value) {
        UNSAFE.putChar(Unsafe.calculateOffset(this, index, CharSpan.SCALE), value);
    }

    public void setShort(long index, short value) {
        UNSAFE.putShort(Unsafe.calculateOffset(this, index, ShortSpan.SCALE), value);
    }

    public void setInt(long index, int value) {
        UNSAFE.putInt(Unsafe.calculateOffset(this, index, IntSpan.SCALE), value);
    }

    public void setLong(long index, long value) {
        UNSAFE.putLong(Unsafe.calculateOffset(this, index, LongSpan.SCALE), value);
    }

    public void setFloat(long index, float value) {
        UNSAFE.putFloat(Unsafe.calculateOffset(this, index, FloatSpan.SCALE), value);
    }

    public void setDouble(long index, double value) {
        UNSAFE.putDouble(Unsafe.calculateOffset(this, index, DoubleSpan.SCALE), value);
    } 
}
