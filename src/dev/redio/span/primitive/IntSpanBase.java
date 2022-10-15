package dev.redio.span.primitive;

import dev.redio.span.PrimitiveSpanBase;
import dev.redio.span.Spans;

public interface IntSpanBase extends PrimitiveSpanBase {

    int get(int index);

    int get(long index);

    default int[] toArray() {
        Spans.checkSpanSize(this);
        int[] result = new int[this.length()];
        for (int i = 0; i < result.length; i++) 
            result[i] = this.get(i);
        return result;
    }
}
