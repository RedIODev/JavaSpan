package dev.redio;

import java.util.Arrays;

import dev.redio.span.CharSequenceSpan;

public class Main {
    public static void main(String[] args) {
        String testString = "fdijiogdpogpdogjdigjpsogkdfogjfdpvjdopvfjbpfobjpfdjbopb";
        for (int i = 0; i < 100_000; i++) {
            testSpanSlice(testString);
        }
        long length = 0;
        for (int i = 0; i < 100_000; i++) {
            length += testSpanSlice(testString);
        }
        System.out.println(length/100_000);
    }

    static long testSubString(String s) {
        String[] buffer = new String[s.length()];
        long start = System.nanoTime();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = s.substring(i, i+1);
        }
        long end = System.nanoTime();
        System.out.println(Arrays.toString(buffer));
        return end-start;
    }

    static long testSpanSlice(String s) {
        CharSequenceSpan cs = new CharSequenceSpan(s);
        CharSequenceSpan[] buffer = new CharSequenceSpan[s.length()];
        long start = System.nanoTime();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = cs.slice(i, 1);
        }
        long end = System.nanoTime();
        System.out.println(Arrays.toString(buffer));
        return end-start;
    }
}
