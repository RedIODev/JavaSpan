
package dev.redio;

import dev.redio.span.primitive.MemoryAccess;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        var array = new int[5];
        var mem = MemoryAccess.of(array);
        var iSpan = mem.asIntSpan();
        iSpan.set(4, 255);
        System.out.println(array[4]);
        
    }
    
}