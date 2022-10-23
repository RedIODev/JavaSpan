package dev.redio.span.primitive;

import java.util.Objects;

public sealed interface MemoryAccess permits HeapMemoryAccess, NativeMemoryAccess {

    public static MemoryAccess of(Object array) {
        return new HeapMemoryAccess(array);
    }

    public static MemoryAccess of(long address, long length) {
        return new NativeMemoryAccess(address, length);
    }


    long length();
    long start();
    MemoryAccess slice(long start, long length);

    byte getByte(long index);
    char getChar(long index);
    short getShort(long index);
    int getInt(long index);
    long getLong(long index);
    float getFloat(long index);
    double getDouble(long index);

    void setByte(long index, byte value);
    void setChar(long index, char value);
    void setShort(long index, short value);
    void setInt(long index, int value);
    void setLong(long index, long value);
    void setFloat(long index, float value);
    void setDouble(long index, double value);

    default ByteSpan asByteSpan() {
        return new ByteSpan(this);
    }

    default CharSpan asCharSpan() {
        return new CharSpan(this);
    }

    default ShortSpan asShortSpan() {
        return new ShortSpan(this);
    }

    default IntSpan asIntSpan() {
        return new IntSpan(this);
    }

    default LongSpan asLongSpan() {
        return new LongSpan(this);
    }

    default FloatSpan asFloatSpan() {
        return new FloatSpan(this);
    }

    default DoubleSpan asDoubleSpan() {
        return new DoubleSpan(this);
    }

    default MemoryAccess.ReadOnly asReadOnly() {
        return new MemoryAccess.ReadOnly(this);
    }

    public static final class ReadOnly {

        private final MemoryAccess span;

        public ReadOnly(MemoryAccess span) {
            this.span = Objects.requireNonNull(span);
        }

        public long bytes() {
            return span.length();
        }

        public byte getByte(long index) {
            return span.getByte(index);
        }

        public char getChar(long index) {
            return span.getChar(index);
        }

        public short getShort(long index) {
            return span.getShort(index);
        }

        public int getInt(long index) {
            return span.getInt(index);
        }

        public long getLong(long index) {
            return span.getLong(index);
        }

        public float getFloat(long index) {
            return span.getFloat(index);
        }

        public double getDouble(long index) {
            return span.getDouble(index);
        }

        public ByteSpan.ReadOnly asByteSpan() {
            return this.span.asByteSpan().asReadOnly();
        }

        public CharSpan.ReadOnly asCharSpan() {
            return this.span.asCharSpan().asReadOnly();
        }

        public ShortSpan.ReadOnly asShortSpan() {
            return this.span.asShortSpan().asReadOnly();
        }

        public IntSpan.ReadOnly asIntSpan() {
            return this.span.asIntSpan().asReadOnly();
        }

        public LongSpan.ReadOnly asLongSpan() {
            return this.span.asLongSpan().asReadOnly();
        }

        public FloatSpan.ReadOnly asFloatSpan() {
            return this.span.asFloatSpan().asReadOnly();
        }

        public DoubleSpan.ReadOnly asDoubleSpan() {
            return this.span.asDoubleSpan().asReadOnly();
        }
        
    }

    
}
