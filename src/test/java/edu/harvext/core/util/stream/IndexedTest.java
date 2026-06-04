package edu.harvext.core.util.stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Class {@link IndexedTest}.
 *
 * @author Gemini
 * @since 2026-06-04
 */
class IndexedTest {

    @Nested
    @DisplayName("Tests for next method")
    class NextMethod {

        @Test
        @DisplayName("Should increment index and update value from an initial state")
        void shouldIncrementIndexAndUpdateValue() {
            // Given
            Indexed<String> initial = new Indexed<>(0, "Start");

            // When
            Indexed<String> next = initial.next("Next");

            // Then
            assertThat(next.index()).isEqualTo(1);
            assertThat(next.value()).isEqualTo("Next");
        }

        @Test
        @DisplayName("Should handle negative starting index correctly")
        void shouldHandleNegativeStartingIndex() {
            Indexed<String> initial = new Indexed<>(-1, null);
            Indexed<String> next = initial.next("First");

            assertThat(next.index()).isZero();
            assertThat(next.value()).isEqualTo("First");
        }
    }

    @Nested
    @DisplayName("Tests for map(Function) method")
    class MapWithFunction {

        @Test
        @DisplayName("Should transform value while keeping the index unchanged")
        void shouldTransformValueAndKeepIndex() {
            // Given
            Indexed<String> indexed = new Indexed<>(5, "123");
            Function<String, Integer> stringToLen = String::length;

            // When
            Indexed<Integer> mapped = indexed.map(stringToLen);

            // Then
            assertThat(mapped.index()).isEqualTo(5);
            assertThat(mapped.value()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Tests for map(BiFunction) method")
    class MapWithBiFunction {

        @Test
        @DisplayName("Should transform value using both index and value, keeping original index")
        void shouldTransformValueUsingIndexAndValue() {
            // Given
            Indexed<String> indexed = new Indexed<>(2, "Apple");
            BiFunction<Integer, String, String> mapper = (idx, val) -> idx + "-" + val;

            // When
            Indexed<String> mapped = indexed.map(mapper);

            // Then
            assertThat(mapped.index()).isEqualTo(2);
            assertThat(mapped.value()).isEqualTo("2-Apple");
        }
    }

    @Nested
    @DisplayName("Tests for unwrap method")
    class UnwrapMethod {

        @Test
        @DisplayName("Should extract and transform inner data directly without returning a new Indexed container")
        void shouldUnwrapAndTransform() {
            // Given
            Indexed<String> indexed = new Indexed<>(10, "Items");
            BiFunction<Integer, String, String> mapper = (idx, val) -> val + " count: " + idx;

            // When
            String result = indexed.unwrap(mapper);

            // Then
            assertThat(result).isEqualTo("Items count: 10");
        }
    }

    @Nested
    @DisplayName("Tests for gatherer factory method")
    class GathererMethod {

        @Test
        @DisplayName("Should correctly index elements across a sequential Stream using the custom gatherer")
        void shouldCorrectlyIndexStreamElements() {
            // Given
            Stream<String> stream = Stream.of("A", "B", "C");

            // When
            List<Indexed<String>> result = stream.gather(Indexed.gatherer()).toList();

            // Then
            assertThat(result)
                    .hasSize(3)
                    .satisfiesExactly(
                            first -> {
                                assertThat(first.index()).isZero();
                                assertThat(first.value()).isEqualTo("A");
                            },
                            second -> {
                                assertThat(second.index()).isEqualTo(1);
                                assertThat(second.value()).isEqualTo("B");
                            },
                            third -> {
                                assertThat(third.index()).isEqualTo(2);
                                assertThat(third.value()).isEqualTo("C");
                            }
                    );
        }
    }
}
