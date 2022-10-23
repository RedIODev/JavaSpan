package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record IntSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_INT.byteSize();

    public IntSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public IntSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public IntSpan slice(long start, long length) {
        final long intStart = start / SCALE;
        final long intLength = length / SCALE;
        final var memory = this.memoryAccess.slice(intStart, intLength);
        return new IntSpan(memory, length);
    }

    public int get(long index) {
        return this.memoryAccess.getInt(index);
    }

    public void set(long index, int value) {
        this.memoryAccess.setInt(index, value);
    }

    public IntSpan.ReadOnly asReadOnly() {
        return new IntSpan.ReadOnly(this);
    }

    public IntSpan.Boxed boxed() {
        return new IntSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final IntSpan span;

        public ReadOnly(IntSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public IntSpan.ReadOnly slice(long start, long length) {
            final long intStart = start / SCALE;
            final long intLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(intStart, intLength);
            return new IntSpan(memory, length).asReadOnly();
        }

        public int get(long index) {
            return this.span.get(index);
        }

        public void set(long index, int value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Integer> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(IntSpan intSpan) implements Span<Integer> {

        @Override
        public long length() {
            return this.intSpan.length;
        }

        @Override
        public IntSpan.Boxed slice(long start, long length) {
            return new IntSpan.Boxed(intSpan.slice(start, length));
        }

        @Override
        public Integer get(long index) {
            return this.intSpan.get(index);
        }

        @Override
        public void set(long index, Integer value) {
            this.intSpan.set(index, value);
        }
    }
}