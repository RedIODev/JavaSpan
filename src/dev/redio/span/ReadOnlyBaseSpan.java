package dev.redio.span;

import java.util.Objects;

public abstract class ReadOnlyBaseSpan<E> 
    implements ReadOnlySpan<E> {
    
    protected final int start;
    protected final int length;

    protected ReadOnlyBaseSpan(int start, int length) {
        this.start = start;
        this.length = length;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ReadOnlyBaseSpan))
            return false;
        ReadOnlyBaseSpan<?> span = (ReadOnlyBaseSpan<?>) obj;
        if (this.length != span.length)
            return false;
        for (int i = 0; i < this.length; i++)
            if (!Objects.equals(this.get(i), span.get(i)))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < this.length; i++)
            h = 31 * h + nullableHashCode(this.get(i));
        return h;
    }

    @Override
    public String toString() {
        if (this instanceof Span)
            return "Span[" + this.length + "]";
        return "ReadOnlySpan[" + this.length + "]";
    }

    private int nullableHashCode(E obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }
}
