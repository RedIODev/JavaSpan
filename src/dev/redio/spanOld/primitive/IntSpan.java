package dev.redio.spanOld.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.spanOld.BoxedSpanBase;
import dev.redio.spanOld.Span;

public interface IntSpan extends IntSpanBase {

    public static IntSpan of(int[] array) {
        return new IAI(array, 0, array.length);
    }

    public static IntSpan of(long baseAddress, long length) {
        return new IUI(baseAddress, length);
    }

    void set(int index, int value);

    void set(long index, int value);

    default void fill(int value) {
        if (this.isOversized()) {
            final long length = this.lengthL();
            for (long i = 0; i < length; i++) 
                this.set(i, value);
        } else {
            final int length = this.length();
            for (int i = 0; i < length; i++) 
                this.set(i, value);
        }
    }

    default void clear() {
        this.fill(0);
    }

    @Override
    Boxed boxed();

    @Override
    IntSpan subSequence(int start);

    @Override
    IntSpan subSequence(int start, int end);

    @Override
    IntSpan subSequence(long start);

    @Override
    IntSpan subSequence(long start, long end);

    ReadOnlyIntSpan asReadOnlySpan();

    public final class Boxed implements BoxedSpanBase<Integer>, Span<Integer> {

        private final IntSpan span;

        Boxed(IntSpan span) {
            this.span = span;
        }

        @Override
        public void set(int index, Integer value) {
            this.span.set(index, value);
        }

        public void set(long index, Integer value) {
            this.span.set(index, value);
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
        public IntSpan unboxed() {
            return this.span;
        }

        @Override
        public ReadOnlyIntSpan.Boxed asReadOnlySpan() {
            return new ReadOnlyIntSpan.Boxed(this.span.asReadOnlySpan());
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

        @Override
        public void fill(Integer value) {
            this.span.fill(value);
        }

        @Override
        public void clear() {
            this.span.clear();
        }

    }
}

final class IAI implements IntSpan {
    final int[] data;
    final int start;
    final int length;

    public IAI(int[] data) {
        this(data, 0);
    }

    public IAI(int[] data, int start) {
        this(data, start, data.length - start);
    }

    public IAI(int[] data, int start, int end) {
        this.data = Objects.requireNonNull(data);
        this.start = Objects.checkFromToIndex(start, end, this.data.length);
        this.length = end - start;
    }

    public IAI(RIAI span) {
        int[] newData = new int[span.length];
        System.arraycopy(span.data, span.start, newData, 0, newData.length);
        this.data = newData;
        this.start = 0;
        this.length = newData.length;
    }

    @Override
    public void set(int index, int value) {
        Objects.checkIndex(index, this.length);
        this.data[index + this.start] = value;
    }

    @Override
    public void set(long index, int value) {
        Objects.checkIndex(index, this.length);
        this.data[(int)index + this.start] = value;
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
    public boolean isOversized() {
        return false;
    }

    @Override
    public ReadOnlyIntSpan asReadOnlySpan() {
        return new RIAI(this);
    }

    @Override
    public IntSpan subSequence(int start) {
        return new IAI(this.data, start);
    }
    @Override
    public IntSpan subSequence(int start, int end) {
        return new IAI(this.data, start, end);
    }
    @Override
    public IntSpan subSequence(long start) {
        return new IAI(this.data, Math.toIntExact(start));
    }
    @Override
    public IntSpan subSequence(long start, long end) {
        return new IAI(this.data, Math.toIntExact(start), Math.toIntExact(end));
    }

    
    
}

final class IUI implements IntSpan {

    private static final long BYTE_SIZE = ValueLayout.JAVA_INT.byteSize();
    final long baseAddress;
    final long length;

    public IUI(long baseAddress, long length) {
        if (!Unsafe.IS_AVAILABLE)
            throw new IllegalStateException("Native memory interface unavailable. sun.misc.Unsafe could not be loaded.");
        this.baseAddress = baseAddress;
        this.length = length;
    }

    private IUI(long baseAddress, long length, long start) {
        this.baseAddress = baseAddress + start * BYTE_SIZE;
        this.length = length;
    }

    @Override
    public void set(int index, int value) {
        this.set((long)index, value);
    }

    @Override
    public void set(long index, int value) {
        Objects.checkIndex(index, this.length);
        Unsafe.UNSAFE.putInt(this.baseAddress + index * BYTE_SIZE, value);
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
    public ReadOnlyIntSpan asReadOnlySpan() {
        return new RIUI(this);
    }

    @Override
    public IntSpan subSequence(int start) {
        return this.subSequence((long)start);
    }

    @Override
    public IntSpan subSequence(int start, int end) {
        return this.subSequence((long)start, (long)end);
    }

    @Override
    public IntSpan subSequence(long start) {
        return this.subSequence(start, this.length);
    }

    @Override
    public IntSpan subSequence(long start, long end) {
        Objects.checkFromToIndex(start, end, this.length);
        return new IUI(this.baseAddress, end - start, start);
    }
}
