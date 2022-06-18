package dev.redio.span.buffer;

import java.nio.CharBuffer;

import dev.redio.span.Span;

public final class CharBufferSpan 
    extends ReadOnlyCharBufferSpan 
    implements Span<Character>, Appendable {

    public CharBufferSpan(CharBuffer charBuffer) {
        super(charBuffer);
    }

    @Override
    public void set(int index, Character value) {
        this.data.put(index,value);
    }

    public void setChar(int index, char value) {
        this.data.put(index,value);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.length(); i++)
            this.setChar(i, (char)0);
    }

    @Override
    public void fill(Character value) {
        for (int i = 0; i < this.data.remaining(); i++) 
            this.set(i, value);
    }

    @Override
    public CharBufferSpan duplicate() {
        return new CharBufferSpan(this.data.duplicate());
    }

    @Override
    public CharBufferSpan slice(int start) {
        return this.slice(start);
    }

    @Override
    public CharBufferSpan slice(int start, int length) {
        return new CharBufferSpan(this.data.slice(start, length));
    }

    @Override
    public CharBufferSpan append(CharSequence csq) {
        this.data.append(csq);
        return this;
    }

    @Override
    public CharBufferSpan append(CharSequence csq, int start, int end) {
        this.data.append(csq, start, end);
        return this;
    }

    @Override
    public CharBufferSpan append(char c) {
        this.data.append(c);
        return this;
    }
}