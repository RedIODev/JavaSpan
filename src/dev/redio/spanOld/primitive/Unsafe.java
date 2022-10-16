package dev.redio.spanOld.primitive;

import java.lang.reflect.Field;

final class Unsafe {
    private Unsafe() {}

    public static final sun.misc.Unsafe UNSAFE;
    public static final boolean IS_AVAILABLE;

    static {
        sun.misc.Unsafe theUnsafe = null;
        try {
            Field theUnsafeField = sun.misc.Unsafe.class.getField("theUnsafe");
            theUnsafeField.setAccessible(true);
            theUnsafe = (sun.misc.Unsafe)theUnsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        UNSAFE = theUnsafe;
        IS_AVAILABLE = UNSAFE != null;
    }
}
