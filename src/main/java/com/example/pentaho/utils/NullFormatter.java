package com.example.pentaho.utils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NullFormatter {

    public static <T, R> void formatNullValue(T obj, Function<T, R> getter, BiConsumer<T, R> setter, Supplier<R> defaultSupplier){
        R value = getter.apply(obj);
        if (value == null) {
            setter.accept(obj, defaultSupplier.get());
        }
    }

}
