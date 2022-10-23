package dev.redio.span.primitive;

import java.lang.reflect.Field;

public final class Unsafe {
    private Unsafe() {}

    public static final sun.misc.Unsafe UNSAFE;
    public static final boolean IS_AVAILABLE;

    static {
        sun.misc.Unsafe theUnsafe = null;
        try {
            Field theUnsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            theUnsafe = (sun.misc.Unsafe)theUnsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
        UNSAFE = theUnsafe;
        IS_AVAILABLE = UNSAFE != null;
    }

    public static long calculateOffset(MemoryAccess access, long index, long scale) {
        final long offset = index * scale;
        if (offset + scale > access.length()) 
            throw new IndexOutOfBoundsException(index);
        return offset + access.start();
    }

    public static void checkLength(MemoryAccess memoryAccess, long length, long scale) {
        final long realLength = memoryAccess.length() / scale;
        if (length > realLength)
            throw new IllegalArgumentException("Length is out of bounds.");
    }
}
