package dev.redio.span;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import dev.redio.span.function.ArrayInsertFunction;
import dev.redio.span.function.IntToCharFunction;

public final class Spans {

    private Spans() {}

    public static boolean baseEquals(Object x, Object y) {
        if (x == null && y == null)
            throw new IllegalArgumentException();
        if (x == y)
            return true;
        if (x == null || y == null) 
            return false;
        return x.getClass() == y.getClass();
    }

    public static boolean equals(CharSequence x, CharSequence y) {
        return equals(x, x::charAt, y, y::charAt);
    }   

    public static boolean equals(CharSequence x, IntToCharFunction xCharFunction, CharSequence y, IntToCharFunction yCharFunction) {
        if (!baseEquals(x, y))
            return false;
        return arrayEquals(x.length(), y.length(), (i -> xCharFunction.applyAsChar(i) == yCharFunction.applyAsChar(i)));
    }

    public static boolean arrayEquals(int xLength, int yLength, IntPredicate elementEquals) {
        if (xLength != yLength)
            return false;
        for (int i = 0; i < xLength; i++) 
            if (!elementEquals.test(i))
                return false;
        return true;
    }

    public static int arrayHashCode(int length, IntUnaryOperator index) {
        int h = 0;
        for (int i = 0; i < length; i++)
            h = 31 * h + index.applyAsInt(i);
        return h;
    }

    public static <T,F> T toArray(F getFunction, int length, IntFunction<T> arrayGenerator, ArrayInsertFunction<T,F> arrayInsertFunction) {
        T array = arrayGenerator.apply(length);
        for (int i = 0; i < length; i++) 
            arrayInsertFunction.insert(array, i, getFunction);
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T,E> T[] toArray(Span<E> span, T[] a) {
        final int length = span.length();
        if (a.length < length)
            a = (T[])Array.newInstance(a.getClass().getComponentType(), length);
        for (int i = 0; i < length; i++) 
            a[i] = (T)span.get(i);
        return a;
    }

    @SuppressWarnings("unchecked")
    public static <T> Span<T> unmodifiableSpan(Span<? extends T> span) {
        if (span instanceof UnmodifiableSpan<? extends T> us)
            return (Span<T>) us;
        return new UnmodifiableSpan<>(span);
    }

    public static <T extends Comparable<T>> int compare(Span<T> x, Span<T> y ) {
        return arrayCompare(x.length(), y.length(), (i -> Objects.requireNonNull(x.get(i)).compareTo(y.get(i))));
    }

    public static int arrayCompare(int lengthX, int lengthY, IntUnaryOperator elementCompare) {
        for (int i = 0, len = Math.min(lengthX, lengthY); i < len; i++) {
            var compareResult = elementCompare.applyAsInt(i);
            if (compareResult != 0)
                return compareResult;
        }
        return lengthX - lengthY;
    }
}
