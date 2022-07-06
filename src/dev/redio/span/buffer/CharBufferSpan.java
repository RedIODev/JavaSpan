package dev.redio.span.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import dev.redio.span.Span;
import dev.redio.span.Spans;

public final class CharBufferSpan 
    extends AbstractBufferSpan<Character,CharBuffer>
    implements Comparable<CharBufferSpan> {

    public CharBufferSpan(CharBuffer charBuffer) {
        super(charBuffer);
    }

    public CharBufferSpan(ByteBuffer byteBuffer) {
        super(byteBuffer.asCharBuffer());
    }

    public char get(int index) {
        return this.data.get(index);
    }

    public void set(int index, char value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        final int length = this.length();
        for (int i = 0; i < length; i++)
            this.set(i, '\0');
    }

    @Override
    public void fill(Character value) {
        final int length = this.length();
        for (int i = 0; i < length; i++)
            this.set(i, value);
    }

    @Override
    public CharBufferSpan slice(int start) {
        return this.slice(start, this.length() - start);
    }

    @Override
    public CharBufferSpan slice(int start, int length) {
        return new CharBufferSpan(this.data.slice(start, length));
    }

    @Override
    public int compareTo(CharBufferSpan o) {
        return Spans.arrayCompare(this.length(), o.length(), (i -> this.get(i) - o.get(i)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ByteBufferSpan other)
            return Spans.arrayEquals(this.length(), other.length(), (i -> this.get(i) == other.get(i)));
        return false;
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::get);
    }

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Character))
            return -1;
        final int length = this.length();
        for (int i = 0; i < length; i++) 
            if(this.get(i) == (char)(Character)o)
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!(o instanceof Character))
            return -1;
        for (int i = this.length()-1; i >= 0; i--) 
            if(this.get(i) == (char)(Character)o)
                return i;
        return -1;
    }

    @Override
    public Span<Character> span() {
        return new Span.Builder<>(this.length(), this::get)
                .setFunction(this::set)
                .build();
    }

    @Override
    public CharBuffer buffer() {
        return this.data.duplicate();
    }
}
