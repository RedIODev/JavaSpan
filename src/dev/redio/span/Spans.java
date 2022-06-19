package dev.redio.span;

import java.lang.reflect.Array;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

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

    public static boolean equals(CharSequence x, IntToCharFunction xcs, CharSequence y, IntToCharFunction ycs) {
        if (!baseEquals(x, y))
            return false;
        return arrayEquals(x.length(), y.length(), (i -> xcs.applyAsChar(i) == ycs.applyAsChar(i)));
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

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(ReadOnlySpan<T> span, T[] a) {
        if (a.length < span.length())
            a = (T[])Array.newInstance(a.getClass().getComponentType(), span.length());
        for (int i = 0; i < span.length(); i++) 
            a[i] = span.get(i);
        return a;
    }

    @FunctionalInterface
    public static interface IntToCharFunction {
        char applyAsChar(int i);
    }

    public static <T extends Comparable<T>> int compare(ReadOnlySpan<T> x, ReadOnlySpan<T> y ) {
        return arrayCompare(x.length(), y.length(), (i -> x.get(i).compareTo(y.get(i))));
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
