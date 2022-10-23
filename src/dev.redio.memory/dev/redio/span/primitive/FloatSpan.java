package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record FloatSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_FLOAT.byteSize();

    public FloatSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public FloatSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public FloatSpan slice(long start, long length) {
        final long floatStart = start / SCALE;
        final long floatLength = length / SCALE;
        final var memory = this.memoryAccess.slice(floatStart, floatLength);
        return new FloatSpan(memory, length);
    }

    public float get(long index) {
        return this.memoryAccess.getFloat(index);
    }

    public void set(long index, float value) {
        this.memoryAccess.setFloat(index, value);
    }

    public FloatSpan.ReadOnly asReadOnly() {
        return new FloatSpan.ReadOnly(this);
    }

    public FloatSpan.Boxed boxed() {
        return new FloatSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final FloatSpan span;

        public ReadOnly(FloatSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public FloatSpan.ReadOnly slice(long start, long length) {
            final long floatStart = start / SCALE;
            final long floatLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(floatStart, floatLength);
            return new FloatSpan(memory, length).asReadOnly();
        }

        public float get(long index) {
            return this.span.get(index);
        }

        public void set(long index, float value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Float> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(FloatSpan floatSpan) implements Span<Float> {

        @Override
        public long length() {
            return this.floatSpan.length;
        }

        @Override
        public FloatSpan.Boxed slice(long start, long length) {
            return new FloatSpan.Boxed(floatSpan.slice(start, length));
        }

        @Override
        public Float get(long index) {
            return this.floatSpan.get(index);
        }

        @Override
        public void set(long index, Float value) {
            this.floatSpan.set(index, value);
        }
    }
}