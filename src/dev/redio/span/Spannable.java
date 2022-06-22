package dev.redio.span;

public interface Spannable<T> {
    
    Span<T> span();

    default Span<T> modifiableSpan() {
        throw new UnsupportedOperationException();
    }
}
