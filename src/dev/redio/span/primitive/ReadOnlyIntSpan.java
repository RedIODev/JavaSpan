package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.BoxedSpanBase;
import dev.redio.span.ReadOnlySpan;

public interface ReadOnlyIntSpan extends IntSpanBase {
    
    public static ReadOnlyIntSpan of(int[] array) {
        return new RIAI(array, 0, array.length);
    }

    public static ReadOnlyIntSpan of(long baseAddress, long length) {
        return new RIUI(baseAddress, length);
    }

    @Override
    Boxed boxed();

    @Override
    ReadOnlyIntSpan subSequence(int start);

    @Override
    ReadOnlyIntSpan subSequence(int start, int end);

    @Override
    ReadOnlyIntSpan subSequence(long start);

    @Override
    ReadOnlyIntSpan subSequence(long start, long end);

    IntSpan toSpan();

    public final class Boxed implements BoxedSpanBase<Integer>, ReadOnlySpan<Integer> {

        private final ReadOnlyIntSpan span;

        Boxed(ReadOnlyIntSpan span) {
            this.span = span;
        }

        @Override
        public Integer get(int index) {
            return this.span.get(index);
        }

        @Override
        public Integer get(long index) {
            return this.span.get(index);
        }

        @Override
        public int length() {
            return this.span.length();
        }

        @Override
        public long lengthL() {
            return this.span.lengthL();
        }

        @Override
        public ReadOnlyIntSpan unboxed() {
            return this.span;
        }

        @Override
        public IntSpan.Boxed toSpan() {
            return new IntSpan.Boxed(this.span.toSpan());
        }

        @Override
        public Boxed subSequence(int start) {
            return new Boxed(this.span.subSequence(start));
        }

        @Override
        public Boxed subSequence(int start, int end) {
            return new Boxed(this.span.subSequence(start, end));
        }

        @Override
        public Boxed subSequence(long start) {
            return new Boxed(this.span.subSequence(start));
        }

        @Override
        public Boxed subSequence(long start, long end) {
            return new Boxed(this.span.subSequence(start, end));
        }

    }
    
}

final class RIAI implements ReadOnlyIntSpan {
    final int[] data;
    final int start;
    final int length;

    public RIAI(int[] data) {
        this(data, 0);
    }

    public RIAI(int[] data, int start) {
        this(data, start, data.length - start);
    }

    public RIAI(int[] data, int start, int end) {
        this.data = Objects.requireNonNull(data);
        this.start = Objects.checkFromToIndex(start, end, this.data.length);
        this.length = end - start;
    }

    public RIAI(IAI span) {
        this.data = span.data;
        this.start = span.start;
        this.length = span.length;
    }

    @Override
    public int get(int index) {
        Objects.checkIndex(index, this.length);
        return this.data[index + this.start];
    }

    @Override
    public int get(long index) {
        Objects.checkIndex(index, this.length);
        return this.data[(int)index + this.start];
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public long lengthL() {
        return this.length;
    }

    @Override
    public Boxed boxed() {
        return new Boxed(this);
    }

    @Override
    public IntSpan toSpan() {
        return new IAI(this);
    }

    @Override
    public boolean isOversized() {
        return false;
    }

    @Override
    public ReadOnlyIntSpan subSequence(int start) {
        return new RIAI(this.data, start);
    }
    @Override
    public ReadOnlyIntSpan subSequence(int start, int end) {
        return new RIAI(this.data, start, end);
    }
    @Override
    public ReadOnlyIntSpan subSequence(long start) {
        return new RIAI(this.data, Math.toIntExact(start));
    }
    @Override
    public ReadOnlyIntSpan subSequence(long start, long end) {
        return new RIAI(this.data, Math.toIntExact(start), Math.toIntExact(end));
    }
    
}

final class RIUI implements ReadOnlyIntSpan {

    private static final long BYTE_SIZE = ValueLayout.JAVA_INT.byteSize();
    final long baseAddress;
    final long length;

    public RIUI(long baseAddress, long length) {
        if (!Unsafe.IS_AVAILABLE)
            throw new IllegalStateException("Native memory interface unavailable. sun.misc.Unsafe could not be loaded.");
        this.baseAddress = baseAddress;
        this.length = length;
    }

    private RIUI(long baseAddress, long length, long start) {
        this.baseAddress = baseAddress + start * BYTE_SIZE;
        this.length = length;
    }

    public RIUI(IUI span) {
        this.baseAddress = span.baseAddress;
        this.length = span.length;
    }

    @Override
    public int get(int index) {
        return this.get((long)index);
    }

    @Override
    public int get(long index) {
        Objects.checkIndex(index, this.length);
        return Unsafe.UNSAFE.getInt(this.baseAddress + index * BYTE_SIZE);
    }

    @Override
    public long lengthL() {
        return this.length;
    }

    @Override
    public int length() {
        return (this.length > Integer.MAX_VALUE) ? -1 : (int)this.length;
    }

    @Override
    public Boxed boxed() {
        return new Boxed(this);
    }

    @Override
    public ReadOnlyIntSpan subSequence(int start) {
        return this.subSequence((long)start);
    }

    @Override
    public ReadOnlyIntSpan subSequence(int start, int end) {
        return this.subSequence((long)start, (long)end);
    }

    @Override
    public ReadOnlyIntSpan subSequence(long start) {
        return this.subSequence(start, this.length);
    }

    @Override
    public ReadOnlyIntSpan subSequence(long start, long end) {
        Objects.checkFromToIndex(start, end, this.length);
        return new RIUI(this.baseAddress, end - start, start);
    }

    @Override
    public IntSpan toSpan() {
        return new IAI(this.toArray());
    }

}
