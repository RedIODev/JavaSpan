package dev.redio.span;

public interface PrimitiveSpanBase extends OversizedSpanBase {
    
    BoxedSpanBase<?> boxed();
}
