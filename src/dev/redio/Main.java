package dev.redio;

import dev.redio.span.Span;
import dev.redio.span.buffer.ByteBufferSpan;
import dev.redio.span.buffer.CharBufferSpan;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // final String data =
        // "fiosdfjifdjgoidfjgoidfjgdfoigvjdfogvjdfgvoidfjgodgjvfdoigvijfdoigfdjgoidfjdfoibvjdfoilyvdsiorokptgerogjrog?vgfdjfgovi";
        // System.console().readLine();
        // //CharSequenceSpan span = ReadOnlySpan.of(data);
        // final int ITERATIONS = 100;
        // char[] sink = new char[ITERATIONS];
        // long start = System.nanoTime();
        // for (int i = 0; i < ITERATIONS; i++) {
        // //CharSequenceSpan s = span.slice(15);
        // String s = data.substring(15);

        // sink[i] = s.charAt(0);
        // }
        // long end = System.nanoTime();
        // System.out.println(end - start);
        List<String> data = new ArrayList<>();
        data.add("Hi");
        data.add("Hallo");
        data.add("LOL");
        System.out.println("Builder:");
        Span<String> span = new Span.Builder<>(data.size() - 1, data::get)
                .start(1)
                .setFunction(data::set)
                .build();
        for (String string : span) {
            System.out.println(string);
        }
        System.out.println("Easy:");
        Span<String> easy = Span.of(data, 1, data.size() - 1);
        for (String string : easy) {
            System.out.println(string);
        }
        
        ByteBufferSpan bbs = new ByteBufferSpan(ByteBuffer.allocateDirect(20));
        CharBufferSpan cbs = bbs.asBufferSpan(CharBufferSpan::new);

        
    }
}
