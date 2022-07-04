package dev.redio.span.function;

@FunctionalInterface
public interface ArrayInsertFunction<T,F> {
    void insert(T array, int index, F getFunction);
}
