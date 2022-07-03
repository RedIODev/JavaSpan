package dev.redio.span.function;

@FunctionalInterface
public interface SetFunction<T> {
    void set(int i, T t);
}