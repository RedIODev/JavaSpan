
package dev.redio;

import java.lang.annotation.Inherited;
import java.util.PrimitiveIterator;

import dev.redio.span.SpanOld;
import dev.redio.span.primitive.IntSpanArrayOld;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        var a = SpanOld.ofA(array);
        var b = a.slice(2,4);
        for (int i = 0; i < b.length(); i++) 
            System.out.println(b.get(i));
    }
}