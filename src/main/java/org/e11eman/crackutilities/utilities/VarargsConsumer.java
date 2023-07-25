package org.e11eman.crackutilities.utilities;

@SuppressWarnings("ALL")
@FunctionalInterface
public interface VarargsConsumer<T> {
    void accept(T... items);
}
