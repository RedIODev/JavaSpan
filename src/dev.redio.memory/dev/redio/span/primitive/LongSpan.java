package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record LongSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_LONG.byteSize();

    public LongSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public LongSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public LongSpan slice(long start, long length) {
        final long longStart = start / SCALE;
        final long longLength = length / SCALE;
        final var memory = this.memoryAccess.slice(longStart, longLength);
        return new LongSpan(memory, length);
    }

    public long get(long index) {
        return this.memoryAccess.getLong(index);
    }

    public void set(long index, long value) {
        this.memoryAccess.setLong(index, value);
    }

    public LongSpan.ReadOnly asReadOnly() {
        return new LongSpan.ReadOnly(this);
    }

    public LongSpan.Boxed boxed() {
        return new LongSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final LongSpan span;

        public ReadOnly(LongSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public LongSpan.ReadOnly slice(long start, long length) {
            final long longStart = start / SCALE;
            final long longLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(longStart, longLength);
            return new LongSpan(memory, length).asReadOnly();
        }

        public long get(long index) {
            return this.span.get(index);
        }

        public void set(long index, long value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Long> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(LongSpan longSpan) implements Span<Long> {

        @Override
        public long length() {
            return this.longSpan.length;
        }

        @Override
        public LongSpan.Boxed slice(long start, long length) {
            return new LongSpan.Boxed(longSpan.slice(start, length));
        }

        @Override
        public Long get(long index) {
            return this.longSpan.get(index);
        }

        @Override
        public void set(long index, Long value) {
            this.longSpan.set(index, value);
        }
    }
}