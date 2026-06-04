package edu.harvext.core.util.stream;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

/**
 * Class {@link Indexed}.
 *
 * @author Erwin Dupont
 * @since 2026-06-04
 */
public record Indexed<T>(int index, T value) {
    private Indexed() {
        this(-1, null);
    }

    public static <T> Gatherer<T, ?, Indexed<T>> gatherer() {
        return Gatherers.scan(Indexed::new, Indexed::next);
    }

    public Indexed<T> next(T value) {
        return new Indexed<>(index + 1, value);
    }

    public <R> Indexed<R> map(Function<T, R> mapper) {
        return new Indexed<>(index, mapper.apply(value));
    }

    public <R> Indexed<R> map(BiFunction<Integer, T, R> mapper) {
        return new Indexed<>(index, this.unwrap(mapper));
    }

    public <R> R unwrap(BiFunction<Integer, T, R> mapper) {
        return mapper.apply(index, value);
    }
}
