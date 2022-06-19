package dev.redio;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.CharSequenceSpan;

public class Main {
    public static void main(String[] args) {
        final String data = "fiosdfjifdjgoidfjgoidfjgdfoigvjdfogvjdfgvoidfjgodgjvfdoigvijfdoigfdjgoidfjdfoibvjdfoilyvdsiorokptgerogjrog?vgfdjfgovi";
        System.console().readLine();
        //CharSequenceSpan span = ReadOnlySpan.of(data);
        final int ITERATIONS = 100;
        char[] sink = new char[ITERATIONS];
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            //CharSequenceSpan s = span.slice(15);
            String s = data.substring(15);
            
            sink[i] = s.charAt(0);
        }
        long end = System.nanoTime();
        System.out.println(end - start);
    }
}
