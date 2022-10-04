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
            final String spaces = " ".repeat(n);
            stream = stream.map(str -> str.concat(spaces));
        } else if (n == Integer.MIN_VALUE) {
            stream = stream.map(str -> str.stripLeading());
        } else if (n < 0) {
            stream = stream.map(str -> str.subSequence(Math.min(-n, str.indexOfNonWhitespace())));
        }
        return Str.of(stream.collect(Collectors.joining("\n", "", "\n")));
    }

    default int indexOf(CharSequence cs) {
        
    }

    default int indexOf(CharSequence cs, int fromIndex) {

    }

    default int indexOf(char c) {

    }

    default int indexOf(char c, int fromIndex) {
        
    }

    default boolean isBlank() {
        
    }

    default int lastIndexOf(CharSequence cs) {
        
    }

    default int lastIndexOf(CharSequence cs, int fromIndex) {

    }

    default int lastIndexOf(char c) {

    }

    default int lastIndexOf(char c, int fromIndex) {
        
    }

    default Stream<Str> lines() {
        
    }

    default boolean matches(String regex) {
        
    }

    default int offsetByCodePoints(int index, int codePointOffset) {
        
    }

    default boolean regionMatches(boolean ignoreCase, int toffset, CharSequence other, int ooffset, int len) {

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

    }

    default Str translateEscapes() {

    }

}
