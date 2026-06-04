package edu.harvext.core.util.function;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Class {@link Builder}.
 *
 * @author Erwin Dupont
 * @since 2017-10-03
 */
@FunctionalInterface
public interface Builder<T> {
    T build();

    static <T> Builder<T> of(Supplier<T> supplier) {
        return supplier::get;
    }

    default Builder<T> with(Consumer<? super T> modifier) {
        return () -> {
            T instance = this.build();
            modifier.accept(instance);
            return instance;
        };
    }

    default <U> Builder<T> with(BiConsumer<? super T, U> modifier, U value) {
        return this.with(instance -> modifier.accept(instance, value));
    }
}
