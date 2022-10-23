package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record DoubleSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_DOUBLE.byteSize();

    public DoubleSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public DoubleSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public DoubleSpan slice(long start, long length) {
        final long doubleStart = start / SCALE;
        final long doubleLength = length / SCALE;
        final var memory = this.memoryAccess.slice(doubleStart, doubleLength);
        return new DoubleSpan(memory, length);
    }

    public double get(long index) {
        return this.memoryAccess.getDouble(index);
    }

    public void set(long index, double value) {
        this.memoryAccess.setDouble(index, value);
    }

    public DoubleSpan.ReadOnly asReadOnly() {
        return new DoubleSpan.ReadOnly(this);
    }

    public DoubleSpan.Boxed boxed() {
        return new DoubleSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final DoubleSpan span;

        public ReadOnly(DoubleSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public DoubleSpan.ReadOnly slice(long start, long length) {
            final long doubleStart = start / SCALE;
            final long doubleLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(doubleStart, doubleLength);
            return new DoubleSpan(memory, length).asReadOnly();
        }

        public double get(long index) {
            return this.span.get(index);
        }

        public void set(long index, double value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Double> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(DoubleSpan doubleSpan) implements Span<Double> {

        @Override
        public long length() {
            return this.doubleSpan.length;
        }

        @Override
        public DoubleSpan.Boxed slice(long start, long length) {
            return new DoubleSpan.Boxed(doubleSpan.slice(start, length));
        }

        @Override
        public Double get(long index) {
            return this.doubleSpan.get(index);
        }

        @Override
        public void set(long index, Double value) {
            this.doubleSpan.set(index, value);
        }
    }
}