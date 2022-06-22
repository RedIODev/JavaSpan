package dev.redio;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.Span;

import java.util.ArrayList;
import java.util.List;

import dev.redio.span.CharSequenceSpan;

public class Main {
    public static void main(String[] args) {
        // final String data = "fiosdfjifdjgoidfjgoidfjgdfoigvjdfogvjdfgvoidfjgodgjvfdoigvijfdoigfdjgoidfjdfoibvjdfoilyvdsiorokptgerogjrog?vgfdjfgovi";
        // System.console().readLine();
        // //CharSequenceSpan span = ReadOnlySpan.of(data);
        // final int ITERATIONS = 100;
        // char[] sink = new char[ITERATIONS];
        // long start = System.nanoTime();
        // for (int i = 0; i < ITERATIONS; i++) {
        //     //CharSequenceSpan s = span.slice(15);
        //     String s = data.substring(15);
            
        //     sink[i] = s.charAt(0);
        // }
        // long end = System.nanoTime();
        // System.out.println(end - start);
        List<String> data = new ArrayList<>();
        data.add("Hi");
        data.add("Hallo");
        data.add("LOL");
        Span<String> span = new Span.Builder<>(data.size(), data::get)
                                .start(1)
                                .setFunction(data::set)
                                .build();
    }
}
