package dev.redio.span;

import java.util.Objects;

import dev.redio.span.function.CharConsumer;

public class CharSequenceSpan
    extends AbstractSpan<Character>
    implements CharSequence, Comparable<CharSequence> {

    private final CharSequence data;

    public CharSequenceSpan(CharSequence cs) {
        this(0, cs.length(), Objects.requireNonNull(cs)); // special constructor without bounds checking.
    }

    public CharSequenceSpan(CharSequence cs, int start, int length) {
        super(start, length);
        Objects.checkFromIndexSize(start, length, cs.length());
        this.data = Objects.requireNonNull(cs);
    }

    protected CharSequenceSpan(int start, int length, CharSequence data) { // special constructor without bounds checking.
        super(start, length);
        this.data = data;
    }

    @Override
    public Character get(int index) {
        return this.data.charAt(this.start + Objects.checkIndex(index, this.length));
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public CharSequenceSpan slice(int start) {
        return this.slice(start, this.length - start);
    }

    @Override
    public CharSequenceSpan slice(int start, int length) {
        Objects.checkFromIndexSize(start, length, this.length);
        return new CharSequenceSpan(this.start + start, length, this.data); // special constructor without bounds checking.
    }

    @Override
    public Character[] toArray() {
        var characterArray = new Character[this.length];
        for (int i = 0; i < this.length; i++)
            characterArray[i] = this.charAtUnchecked(this.start + i);
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

    public void forEachChar(CharConsumer action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < this.length; i++)
            action.accept(this.charAtUnchecked(i));
    }

    @Override
    public String toString() {
        return this.data.subSequence(this.start, this.start + this.length).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharSequenceSpan ss)
            return Spans.equals(this, this::charAtUnchecked, ss, ss::charAtUnchecked);
        return false;
    }

    @Override
    public int hashCode() {
        return Spans.arrayHashCode(this.length, this::charAtUnchecked);
    }

    @Override
    public int compareTo(CharSequence cs) {
        if (cs instanceof CharSequenceSpan ss)
            return Spans.arrayCompare(this.length, ss.length, (i -> this.charAtUnchecked(i) - ss.charAtUnchecked(i)));
        return Spans.arrayCompare(this.length, cs.length(), (i -> this.charAtUnchecked(i) - cs.charAt(i)));
    }

    protected char charAtUnchecked(int index) {
        return this.data.charAt(this.start + index);
    }
}
