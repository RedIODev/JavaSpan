package dev.redio.span;

import dev.redio.pointer.Keywords;
import dev.redio.pointer.Pointer;
import dev.redio.pointer.Utils;

/**
 * Span
 */
public class Span<T> {
    private final Pointer<T> ptr;
    private final int baseOffset;
    private final int maxLength;
    private int start;
    private int length;
    
    public Span(T[] array) {
        
    }
    
}