package dev.redio.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Str 
    extends Comparable<Str>, CharSequence {
    
    public static Str of(CharSequence cs) {
        class StrImpl implements Str {
            private final CharSequence cs;

            StrImpl(CharSequence cs) {
                this.cs = cs;
            }

            @Override
            public int length() {
                return cs.length();
            }

            @Override
            public char charAt(int index) {
                return cs.charAt(index);
            }

            @Override
            public Str subSequence(int start, int end) {
                return new StrImpl(this.cs.subSequence(start, end));
            }
        }

        return new StrImpl(cs);

    }

    public static CharSequence csConcat(CharSequence c1, CharSequence c2) {
        class LinkedSequence implements CharSequence {
            
            private final CharSequence c1;
            private final CharSequence c2;

            LinkedSequence(CharSequence c1, CharSequence c2) {
                this.c1 = c1;
                this.c2 = c2;
            }

            @Override
            public int length() {
                return this.c1.length() + this.c2.length();
            }

            @Override
            public char charAt(int index) {
                Objects.checkIndex(index, this.length());
                final int firstLength = this.c1.length();
                return (index < firstLength) ? 
                        this.c1.charAt(index) : 
                        this.c2.charAt(index - firstLength);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                final int firstLength = this.c1.length();
                if (start > firstLength)
                    return this.c2.subSequence(start - firstLength, end - firstLength);
                if (end <= firstLength) 
                    return this.c1.subSequence(start, end);
                return new LinkedSequence(
                       this.c1.subSequence(start, firstLength),
                       this.c2.subSequence(0, end - firstLength));
            }
        }

        return new LinkedSequence(c1, c2);
    }

    default int codePointAt(int index) {
        return Character.codePointAt(this, index);
    }

    default int codePointBefore(int index) {
        return Character.codePointBefore(this, index);
    }

    default int codePointCount(int beginIndex, int endIndex) {
        return Character.codePointCount(this, beginIndex, endIndex);
    }


    @Override
    default int compareTo(Str other) {
        final int length = Math.min(this.length(), other.length());
        for (int i = 0; i < length; i++) {
            int dif = this.charAt(i) - other.charAt(i);
            if (dif == 0)
                continue;
            return dif;
        }
        
        return this.length() - other.length();
    }

    default int compareToIgnoreCase(CharSequence other) {
        final int length = Math.min(this.length(), other.length());
        for (int i = 0; i < length; i++) {
            int dif = Character.toLowerCase(this.charAt(i)) - 
                      Character.toLowerCase(other.charAt(i));
            if (dif == 0)
                continue;
            return dif;
        }
        return this.length() - other.length();
    }

    default Str concat(CharSequence other) {
        return Str.of(Str.csConcat(this, other));
    }

    default boolean contains(CharSequence cs) {
        return this.indexOf(cs) >= 0;
    }

    default boolean contentEquals(CharSequence cs) {
        final int length = this.length();
        if (length != cs.length())
            return false;
        for (int i = 0; i < length; i++) 
            if (this.charAt(i) != cs.charAt(i))
                return false;
        return true;
    }

    default boolean endsWith(CharSequence suffix) {
        return this.startsWith(suffix, this.length() - suffix.length());
    }

    @Override
    boolean equals(Object obj);

    default boolean equalsIgnoreCase(CharSequence cs) {
        return this.compareToIgnoreCase(cs) == 0;
    }

    default Str formatted(Object ... args) {
        try(var f = new Formatter()) {
            return Str.of(f.format(this.toString(), args).toString());
        }
    }

    default byte[] getBytes() {
        return this.toString().getBytes();
    }

    default byte[] getBytes(Charset charset) {
        return this.toString().getBytes(charset);
    }

    default char[] getChars() {
        var result = new char[this.length()];
        for (int i = 0; i < result.length; i++) 
            result[i] = this.charAt(i);
        return result;
    }

    default Str indent(int n) {
        if (isEmpty())
            return this;
        var stream = this.lines();
        if (n > 0) {
            final Str spaces = Str.of(" ").repeat(n);
            stream = stream.map(str -> str.concat(spaces));
        } else if (n == Integer.MIN_VALUE) {
            stream = stream.map(str -> str.stripLeading());
        } else if (n < 0) {
            stream = stream.map(str -> str.subSequence(Math.min(-n,str.indexOfNonWhitespace())));
        }
        return Str.of(stream.collect(Collectors.joining("\n", "", "\n")));
    }

    default int indexOf(CharSequence cs) {
        return this.indexOf(cs, 0);
    }

    default int indexOf(CharSequence cs, int fromIndex) {
        final int length = this.length();
        if (cs.length() == 0)
            return -1;
        final char firstChar = cs.charAt(0);
        for (int i = fromIndex - 1; i < length;) {
            i = this.indexOf(firstChar, i + 1);
            if (i == -1)
                return -1;
            if (this.isEqualSequence(i, cs))
                return i;
        }
        return -1;
    }

    default int indexOf(char c) {
        return this.indexOf(c, 0);
    }

    default int indexOf(char c, int fromIndex) {
        final int length = this.length();
        for (int i = fromIndex; i < length; i++) 
            if (this.charAt(i) == c)
                return i;
        return -1;
    }

    default boolean isBlank() {
        return this.indexOfNonWhitespace() == this.length();
    }

    default int lastIndexOf(CharSequence cs) {
        return this.lastIndexOf(cs, this.length() - 1);
    }

    default int lastIndexOf(CharSequence cs, int fromIndex) {
        if (cs.length() == 0)
            return -1;
        final char lastChar = cs.charAt(cs.length() - 1);
        for (int i = fromIndex + 1; i >= 0;) {
            i = this.lastIndexOf(lastChar, i - 1);
            if (i == -1)
                return -1;
            if (this.isEqualSequenceReverse(i, cs))
                return i;
        }
        return -1;
    }

    default int lastIndexOf(char c) {
        return this.lastIndexOf(c, this.length() - 1);
    }

    default int lastIndexOf(char c, int fromIndex) {
        for (int i = this.length() - 1; i >= 0; i--) 
            if (this.charAt(i) == c)
                return i;
        return -1;
    }

    default Stream<Str> lines() {
        final int length = this.length();
        var list = new ArrayList<Str>();
        for (int i = -1, lastIndex = 0; i < length;) {
            i = indexOf('\n', i + 1);
            if (i == -1) {
                list.add(this.subSequence(lastIndex, length));
                break;
            }
            list.add(this.subSequence(lastIndex, i));
            if (i + 1 < length && this.charAt(i + 1) == '\r')
                i++;
            lastIndex = i + 1;
        }
        return list.stream();
        "".lines();
    }

    default boolean matches(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(this);
        return m.matches();
    }

    default int offsetByCodePoints(int index, int codePointOffset) {
        if (index < 0 || index > length()) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePoints(this, index, codePointOffset);
    }

    default boolean regionMatches(boolean ignoreCase, int toffset, CharSequence other, int ooffset, int len) {
        "".regionMatches(ignoreCase, toffset, other, ooffset, len)
    }

    default boolean regionMatches(int toffset, CharSequence other, int ooffset, int len) {

    }

    default Str repeat(int count) {
        
    }

    default Str replace(CharSequence target, CharSequence replacement) {

    }

    default Str replace(char target, char replacement) {
        
    }

    default Str replaceAll(String regex, CharSequence replacement) {

    }

    default Str replaceFirst(String regex, CharSequence replacement) {
        
    }

    default Str[] split(String regex) {
        return this.split(regex, 0);
    }

    default Str[] split(String regex, int limit) {
        int index = 0;
        boolean matchLimited = limit > 0;
        ArrayList<Str> matchList = new ArrayList<>();
        Matcher m = Pattern.compile(regex).matcher(this);

        // Add segments before each match found
        while(m.find()) {
            if (!matchLimited || matchList.size() < limit - 1) {
                if (index == 0 && index == m.start() && m.start() == m.end()) {
                    // no empty leading substring included for zero-width match
                    // at the beginning of the input char sequence.
                    continue;
                }
                Str match = this.subSequence(index, m.start());
                matchList.add(match);
                index = m.end();
            } else if (matchList.size() == limit - 1) { // last one
                Str match = this.subSequence(index, this.length());
                matchList.add(match);
                index = m.end();
            }
        }

        // If no match was found, return this
        if (index == 0)
            return new Str[] {this};

        // Add remaining segment
        if (!matchLimited || matchList.size() < limit)
            matchList.add(this.subSequence(index, this.length()));

        // Construct result
        int resultSize = matchList.size();
        if (limit == 0)
            while (resultSize > 0 && matchList.get(resultSize-1).isEmpty())
                resultSize--;
        Str[] result = new Str[resultSize];
        return matchList.subList(0, resultSize).toArray(result);
    }

    default boolean startsWith(CharSequence prefix) {

    }

    default boolean startsWith(CharSequence prefix, int toffset) {
        
    }

    default Str strip() {

    }

    default Str stripIndent() {

    }

    default Str stripLeading() {

    }

    default Str stripTrailing() {
        
    }

    @Override
    Str subSequence(int start, int end);

    default Str subSequence(int start) {
        return subSequence(start, this.length());
    }

    default Str toLowerCase() {

    }

    default Str toLowerCase(Locale locale) {

    }

    @Override
    String toString();

    default Str toUpperCase() {

    }

    default Str toUpperCase(Locale locale) {
        
    }

    default <R> R transform(Function<? super Str, ? extends R> f) {
        return f.apply(this);
    }

    default Str translateEscapes() {

    }

    private int indexOfNonWhitespace() {
        final int length = this.length();
        for (int i = 0; i < length;) {
            int codePoint = this.codePointAt(i);
            if (codePoint != ' ' && codePoint != '\t' && !Character.isWhitespace(codePoint))
                return i;
            i += Character.charCount(codePoint);
        }
        return 0;
    }

    private boolean isEqualSequence(int fromIndex, CharSequence cs) {
        final int length = cs.length();
        if (this.length() - fromIndex < length)
            return false;
        for (int i = 0; i < length; i++) 
            if (this.charAt(i + fromIndex) != cs.charAt(i))
                return false;
        return true;
    }

    private boolean isEqualSequenceReverse(int fromIndex, CharSequence cs) {
        if (fromIndex - cs.length() < 0)
            return false;
        for (int i = cs.length() - 1, j = fromIndex; i >= 0; i--, j--) 
            if (this.charAt(j) != cs.charAt(i))
                return false;
        return true;
    }

}
