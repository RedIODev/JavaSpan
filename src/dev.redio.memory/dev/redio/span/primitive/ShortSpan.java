package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record ShortSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_SHORT.byteSize();

    public ShortSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public ShortSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public ShortSpan slice(long start, long length) {
        final long shortStart = start / SCALE;
        final long shortLength = length / SCALE;
        final var memory = this.memoryAccess.slice(shortStart, shortLength);
        return new ShortSpan(memory, length);
    }

    public short get(long index) {
        return this.memoryAccess.getShort(index);
    }

    public void set(long index, short value) {
        this.memoryAccess.setShort(index, value);
    }

    public ShortSpan.ReadOnly asReadOnly() {
        return new ShortSpan.ReadOnly(this);
    }

    public ShortSpan.Boxed boxed() {
        return new ShortSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final ShortSpan span;

        public ReadOnly(ShortSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public ShortSpan.ReadOnly slice(long start, long length) {
            final long shortStart = start / SCALE;
            final long shortLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(shortStart, shortLength);
            return new ShortSpan(memory, length).asReadOnly();
        }

        public short get(long index) {
            return this.span.get(index);
        }

        public void set(long index, short value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Short> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(ShortSpan shortSpan) implements Span<Short> {

        @Override
        public long length() {
            return this.shortSpan.length;
        }

        @Override
        public ShortSpan.Boxed slice(long start, long length) {
            return new ShortSpan.Boxed(shortSpan.slice(start, length));
        }

        @Override
        public Short get(long index) {
            return this.shortSpan.get(index);
        }

        @Override
        public void set(long index, Short value) {
            this.shortSpan.set(index, value);
        }
    }
}