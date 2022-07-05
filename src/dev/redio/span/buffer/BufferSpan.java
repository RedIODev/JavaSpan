package dev.redio.span.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.function.Function;

import dev.redio.span.Span;

public interface BufferSpan<E,B extends Buffer>
    extends Span<E> {

    static BufferSpan<Byte,ByteBuffer> of(ByteBuffer buffer) {

    }

    default byte getByte(int index) {
        throw new UnsupportedOperationException();
    }

    default void setByte(int index, byte value) {
        throw new UnsupportedOperationException();
    }

    default char getChar(int index) {
        throw new UnsupportedOperationException();
    }

    default void setChar(int index, char value) {
        throw new UnsupportedOperationException();
    }

    default double getDouble(int index) {
        throw new UnsupportedOperationException();
    }

    default void setDouble(int index, double value) {
        throw new UnsupportedOperationException();
    }

    default float getFloat(int index) {
        throw new UnsupportedOperationException();
    }

    default void setFloat(int index, float value) {
        throw new UnsupportedOperationException();
    }

    default int getInt(int index) {
        throw new UnsupportedOperationException();
    }

    default void setInt(int index, int value) {
        throw new UnsupportedOperationException();
    }

    default long getLong(int index) {
        throw new UnsupportedOperationException();
    }

    default void setLong(int index, long value) {
        throw new UnsupportedOperationException();
    }

    default short getShort(int index) {
        throw new UnsupportedOperationException();
    }

    default void setShort(int index, short value) {
        throw new UnsupportedOperationException();
    }

    default <T,U extends Buffer> BufferSpan<T,U> changeType(Function<B,U> converter) {
        throw new UnsupportedOperationException();
    }
}
