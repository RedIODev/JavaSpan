package dev.redio.span.function;

@FunctionalInterface
public interface GetFunction<T> {
    T get(int i);
}