package dev.redio.span;

import java.util.Objects;

import dev.redio.span.primitive.ByteSpan;
import dev.redio.span.primitive.CharSpan;
import dev.redio.span.primitive.DoubleSpan;
import dev.redio.span.primitive.FloatSpan;
import dev.redio.span.primitive.IntSpan;
import dev.redio.span.primitive.LongSpan;
import dev.redio.span.primitive.ShortSpan;
import dev.redio.utils.Modifiable;

public sealed interface Span<E> 
    extends SpanBase<E>, Modifiable<E> 
    permits SpanImpl, ByteSpan.Boxed, CharSpan.Boxed, ShortSpan.Boxed, IntSpan.Boxed, LongSpan.Boxed, FloatSpan.Boxed, DoubleSpan.Boxed {
    
    long length();

    Span<E> slice(long start, long length);

    E get(long index);

    void set(long index, E value);

    default Span.ReadOnly<E> asReadOnly() {
        return new Span.ReadOnly<>(this);
    }

    public static final class ReadOnly<E> implements SpanBase<E> {
        private final Span<E> span;

        public ReadOnly(Span<E> span) {
            this.span = Objects.requireNonNull(span);
        }

        public Span.ReadOnly<E> slice(long start, long length) {
            return this.span.slice(start, length).asReadOnly();
        }

        @Override
        public long length() {
            return this.span.length();
        }

        public E get(long index) {
            return this.span.get(index);
        }


    }
}
