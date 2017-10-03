package edu.harvext.core.util.stream;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static edu.harvext.core.util.stream.SingleElementCollector.exactlyOne;
import static edu.harvext.core.util.stream.SingleElementCollector.toOptional;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Class {@link SingleElementCollectorTest}.
 *
 * @author Erwin Dupont
 * @since 2017-10-03
 */
class SingleElementCollectorTest {
    @Test
    void toOptionalReturnsPresentOptionalInSingleElementStream() {
        String single = "single";

        Optional<String> result = of(single).collect(toOptional());
        assertThat(result).isPresent().containsSame(single);
    }

    @Test
    void toOptionalReturnsEmptyOptionalInEmptyStream() {
        Optional<String> result = Stream.<String>of().collect(toOptional());
        assertThat(result).isEmpty();
    }

    @Test
    void toOptionalThrowsExceptionInMultipleElementsStream() {
        assertThatThrownBy(() -> of("one", "two").collect(toOptional()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only one element expected.");
    }

    @Test
    void toOptionalThrowsCustomisedExceptionInMultipleElementsStream() {
        assertThatThrownBy(() -> of("one", "two").collect(toOptional(() -> new IllegalStateException("Too many"))))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Too many");
    }

    @Test
    void exactlyOneReturnsSingleElementInSingleElementStream() {
        String single = "single";

        String result = of(single).collect(exactlyOne());
        assertThat(result).isSameAs(single);
    }

    @Test
    void exactlyOneThrowsExceptionInEmptyStream() {
        assertThatThrownBy(() -> Stream.<String>of().collect(exactlyOne()))
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessage("Exactly one element expected.");
    }

    @Test
    void exactlyOneThrowsCustomisedExceptionInEmptyStream() {
        assertThatThrownBy(() -> Stream.<String>of().collect(exactlyOne(() -> new IllegalStateException("Too many"), () -> new UnsupportedOperationException("Too few"))))
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Too few");
    }

    @Test
    void exactlyOneThrowsExceptionInMultipleElementsStream() {
        assertThatThrownBy(() -> of("one", "two").collect(exactlyOne()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only one element expected.");
    }

    @Test
    void exactlyOneThrowsCustomisedExceptionInMultipleElementsStream() {
        assertThatThrownBy(() -> of("one", "two").collect(exactlyOne(() -> new IllegalStateException("Too many"), () -> new UnsupportedOperationException("Too few"))))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Too many");
    }
}