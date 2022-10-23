package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record ByteSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_BYTE.byteSize();

    public ByteSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public ByteSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }

    public ByteSpan slice(long start, long length) {
        final long byteStart = start / SCALE;
        final long byteLength = length / SCALE;
        final var memory = this.memoryAccess.slice(byteStart, byteLength);
        return new ByteSpan(memory, length);
    }

    public byte get(long index) {
        return this.memoryAccess.getByte(index);
    }

    public void set(long index, byte value) {
        this.memoryAccess.setByte(index, value);
    }

    public ByteSpan.ReadOnly asReadOnly() {
        return new ByteSpan.ReadOnly(this);
    }

    public ByteSpan.Boxed boxed() {
        return new ByteSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final ByteSpan span;

        public ReadOnly(ByteSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public ByteSpan.ReadOnly slice(long start, long length) {
            final long byteStart = start / SCALE;
            final long byteLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(byteStart, byteLength);
            return new ByteSpan(memory, length).asReadOnly();
        }

        public byte get(long index) {
            return this.span.get(index);
        }

        public void set(long index, byte value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Byte> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(ByteSpan byteSpan) implements Span<Byte> {

        @Override
        public long length() {
            return this.byteSpan.length;
        }

        @Override
        public ByteSpan.Boxed slice(long start, long length) {
            return new ByteSpan.Boxed(byteSpan.slice(start, length));
        }

        @Override
        public Byte get(long index) {
            return this.byteSpan.get(index);
        }

        @Override
        public void set(long index, Byte value) {
            this.byteSpan.set(index, value);
        }
    }
}
