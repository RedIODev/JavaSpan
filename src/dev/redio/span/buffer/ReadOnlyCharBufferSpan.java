package dev.redio.span.buffer;

import java.io.IOException;
import java.nio.CharBuffer;

import dev.redio.span.Spans;

public sealed class ReadOnlyCharBufferSpan 
    extends ReadOnlyBufferSpan<Character,CharBuffer> 
    implements Comparable<ReadOnlyCharBufferSpan>, CharSequence, Readable
    permits CharBufferSpan {

    public ReadOnlyCharBufferSpan(CharBuffer charBuffer) {
        super(charBuffer.duplicate());
    }

    @Override
    public Character get(int index) {
        return this.data.get(index);
    }

    public char getChar(int index) {
        return this.data.get(index);
    }

    @Override
    public ReadOnlyCharBufferSpan duplicate() {
        return new ReadOnlyCharBufferSpan(this.data.duplicate());
    }

    @Override
    public ReadOnlyCharBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public ReadOnlyCharBufferSpan slice(int start, int length) {
        return new ReadOnlyCharBufferSpan(this.data.slice(start, length));
    }

    public char[] toCharArray() {
        var charArray = new char[this.data.remaining()];
        for (int i = 0; i < this.data.remaining(); i++)
            charArray[i] = this.data.get(i);
        return charArray;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public char charAt(int index) {
        return this.data.get(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.slice(start, end - start);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.data.remaining(); i++) 
            sb.append(this.data.get(i));
        return sb.toString();
    }

    @Override
    public int read(CharBuffer cb) throws IOException {
        return this.data.read(cb);
    }

    @Override
    public int compareTo(ReadOnlyCharBufferSpan o) {
        return CharSequence.compare(this, o);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharSequence cs) 
            return Spans.equals(this, cs);
        return false;
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length(), this::getChar);
    }
}
