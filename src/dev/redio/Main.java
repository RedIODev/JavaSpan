package dev.redio;

import dev.redio.span.ReadOnlySpan;
import dev.redio.span.StringSpan;

public class Main {
    public static void main(String[] args) {
        final String data = "fiosdfjifdjgoidfjgoidfjgdfoigvjdfogvjdfgvoidfjgodgjvfdoigvijfdoigfdjgoidfjdfoibvjdfoilyvdsiovgfdjfgovi";
        System.console().readLine();
        StringSpan span = ReadOnlySpan.of(data);
        for (int i = 0; i < 10_000; i++) {
            StringSpan s = span.slice(15);
            //String s = data.substring(15);
            System.out.println(s);
        }
    }
}
