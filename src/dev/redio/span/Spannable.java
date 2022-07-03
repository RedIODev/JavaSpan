package dev.redio.span;

public interface Spannable<T> {
    
    Span<T> span();

    default Span<T> unmodifiableSpan() {
        return Spans.unmodifiableSpan(this.span());
    }
}
