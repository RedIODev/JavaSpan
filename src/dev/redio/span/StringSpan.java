package dev.redio.span;

import java.util.Objects;

public final class StringSpan 
    extends ReadOnlyBaseSpan<Character> 
    implements CharSequence {

    private final String data;
    private int hash;
    private boolean isHashCached = false;

    public StringSpan(String string) {
        this(string, 0, string.length());
    }

    public StringSpan(String string, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, this.length);
        this.data = Objects.requireNonNull(string);
    }

    @Override
    public Character get(int index) {
        return this.data.charAt(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public StringSpan duplicate() {
        return new StringSpan(this.data, this.start, this.length);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public StringSpan slice(int start) {
        return this.slice(start, this.length - start);
    }

    @Override
    public StringSpan slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new StringSpan(this.data, this.start + start, length);
    }

    @Override
    public Character[] toArray() {
        var characterArray = new Character[this.length];
        for (int i = 0; i < this.length; i++) 
            characterArray[i] = this.data.charAt(this.start + i);
        return characterArray;
    }

    @Override
    public char charAt(int index) {
        return this.data.charAt(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.slice(start, end - start);
    }

    @Override
    public String toString() {
        return this.data.substring(this.start, this.start + this.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringSpan ss) 
            return Spans.equals(this, this::charAtUnchecked, ss, ss::charAtUnchecked);
        return false;
    }

    @Override
    public int hashCode() {
        if (isHashCached) 
            return hash;
        int h = Spans.arrayHashCode(this.length, this::charAtUnchecked);
        this.hash = h;
        this.isHashCached = true;
        return h;
    }

    private char charAtUnchecked(int index) {
        return this.data.charAt(this.start + index);
    }
    
}
