package dev.redio.span.primitive;

import java.lang.foreign.ValueLayout;
import java.util.Objects;

import dev.redio.span.Span;

public record CharSpan(MemoryAccess memoryAccess, long length) {
    static final long SCALE = ValueLayout.JAVA_CHAR.byteSize();

    public CharSpan {
        Objects.requireNonNull(memoryAccess);
        Unsafe.checkLength(memoryAccess, length, SCALE);
    }

    public CharSpan(MemoryAccess memoryAccess) {
        this(memoryAccess, memoryAccess.length() / SCALE);
    }
    
    public CharSpan slice(long start, long length) {
        final long byteStart = start / SCALE;
        final long byteLength = length / SCALE;
        final var memory = this.memoryAccess.slice(byteStart, byteLength);
        return new CharSpan(memory, length);
    }

    public char get(long index) {
        return this.memoryAccess.getChar(index);
    }

    public void set(long index, char value) {
        this.memoryAccess.setChar(index, value);
    }

    public CharSpan.ReadOnly asReadOnly() {
        return new CharSpan.ReadOnly(this);
    }

    public CharSequence asCharSequence() {
        final class Sequence implements CharSequence {
            private final CharSpan span;

            public Sequence(CharSpan span) {
                this.span = span;
            }
            @Override
            public int length() {
                return (this.span.length > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)this.span.length;
            }

            @Override
            public char charAt(int index) {
                return this.span.get(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new Sequence(this.span.slice(start, (long)end - (long)start));
            }

            @Override
            public String toString() {
                var builder = new StringBuilder();
                final int length = this.length();
                for (int i = 0; i < length; i++) 
                    builder.append(this.charAt(i));
                return builder.toString();
            }
        }
        return new Sequence(this);
    }

    public CharSpan.Boxed boxed() {
        return new CharSpan.Boxed(this);
    }

    public static final class ReadOnly {
        private final CharSpan span;

        public ReadOnly(CharSpan span) {
            this.span = Objects.requireNonNull(span);
        }

        public CharSpan.ReadOnly slice(long start, long length) {
            final long byteStart = start / SCALE;
            final long byteLength = length / SCALE;
            final var memory = this.span.memoryAccess.slice(byteStart, byteLength);
            return new CharSpan(memory, length).asReadOnly();
        }

        public char get(long index) {
            return this.span.get(index);
        }

        public void set(long index, char value) {
            this.span.set(index, value);
        }

        public MemoryAccess.ReadOnly memoryAccess() {
            return this.span.memoryAccess.asReadOnly();
        }

        public Span.ReadOnly<Character> boxed() {
            return this.span.boxed().asReadOnly();
        }
    }

    public static record Boxed(CharSpan charSpan) implements Span<Character> {

        @Override
        public long length() {
            return this.charSpan.length;
        }

        @Override
        public CharSpan.Boxed slice(long start, long length) {
            return new CharSpan.Boxed(charSpan.slice(start, length));
        }

        @Override
        public Character get(long index) {
            return this.charSpan.get(index);
        }

        @Override
        public void set(long index, Character value) {
            this.charSpan.set(index, value);
        }
    }
}
