package dev.redio.span;

import java.util.Collection;

public interface PrimitiveSpan<Self extends PrimitiveSpan<Self>> {
    
    int length();

    default void clear() {
        this.fill(null);
    }

    void fill(Object value);

    default boolean contains(Object o) {
        return this.indexOf(o) >=0;
    }

    default boolean containsAll(Collection<?> c) {
        for (Object e : c) 
            if (!this.contains(e))
                return false;
        return true;
    }

    boolean copyFrom()

    int indexOf(Object o);
}
