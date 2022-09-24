
package dev.redio;

import dev.redio.span.Span;
import dev.redio.span.primitive.IntSpanArray;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        int[] array = {0,1,2,3,4,5,6,7,8,9};
        var a = Span.ofA(array);
        var b = a.slice(2,4);
        for (int i = 0; i < b.size(); i++) 
            System.out.println(b.get(i));
        
    }
}