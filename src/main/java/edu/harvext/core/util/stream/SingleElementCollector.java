package edu.harvext.core.util.stream;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Class {@link SingleElementCollector}.
 *
 * @author Erwin Dupont
 * @since 2017-10-03
 */
public final class SingleElementCollector {

    private SingleElementCollector() {
    }

    public static <T> Collector<T, ?, Optional<T>> toOptional() {
        return toOptional(() -> new IllegalArgumentException("Only one element expected."));
    }

    public static <T> Collector<T, ?, Optional<T>> toOptional(Supplier<? extends RuntimeException> whenMore) {
        return Collectors.reducing(new OnlyOne<>(whenMore));
    }

    public static <T> Collector<T, ?, T> exactlyOne() {
        return exactlyOne(() -> new IllegalArgumentException("Only one element expected."), () -> new NoSuchElementException("Exactly one element expected."));
    }

    public static <T> Collector<T, ?, T> exactlyOne(Supplier<? extends RuntimeException> whenMore, Supplier<? extends RuntimeException> whenNone) {
        return Collectors.collectingAndThen(toOptional(whenMore), new OrElseThrow<>(whenNone));
    }

    private static class OrElseThrow<T> implements Function<Optional<T>, T> {
        private final Supplier<? extends RuntimeException> exceptionSupplier;

        private OrElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
            this.exceptionSupplier = exceptionSupplier;
        }

        @Override
        public T apply(Optional<T> optional) {
            return optional.orElseThrow(exceptionSupplier);
        }
    }

    private static class OnlyOne<T> implements BinaryOperator<T> {
        private final Supplier<? extends RuntimeException> exceptionSupplier;

        private OnlyOne(Supplier<? extends RuntimeException> exceptionSupplier) {
            this.exceptionSupplier = exceptionSupplier;
        }

        @Override
        public T apply(T first, T second) {
            throw exceptionSupplier.get();
        }
    }
}
