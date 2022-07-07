package dev.redio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

import dev.redio.span.CharSequenceSpan;
import dev.redio.span.buffer.BufferSpan;
import dev.redio.span.buffer.ByteBufferSpan;
import dev.redio.span.buffer.CharBufferSpan;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 100_000; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024*1024);
            for (int j = 0; j < 1024*1024*1024; j++) {
                buffer.put(j,(byte)1);
            }
            buffer = null;
        }
        // String testString = "fdijiogdpogpdogjdigjpsogkdfogjfdpvjdopvfjbpfobjpfdjbopb";
        // for (int i = 0; i < 100_000; i++) {
        //     testSpanSlice(testString);
        // }
        // long length = 0;
        // for (int i = 0; i < 100_000; i++) {
        //     length += testSpanSlice(testString);
        // }
        // System.out.println(length/100_000);



        // ByteBufferSpan bbs = new ByteBufferSpan(ByteBuffer.allocateDirect(20));
        // CharBufferSpan cbs = bbs.as(CharBufferSpan::new);
        // for (int i = 0; i < cbs.length(); i++) {
        //     cbs.set(i, (char)('A' + i));
        // }
        // System.out.println(cbs);
        // for (int i = 0; i < bbs.length(); i++) {
        //     System.out.print(bbs.get(i));
        //     System.out.print(", ");
        // }
        // CharBufferSpan subSpan = cbs.slice(5,2);
        // System.out.println(subSpan);
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
