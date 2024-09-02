package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.common.Nameable;
import java.math.BigDecimal;

/**
 * The Symbol interface represents a named mathematical entity that holds a value of type {@code T}.
 * <p>
 * This interface extends {@link Nameable}, meaning each symbol has a unique name associated with it.
 * It is commonly used in mathematical parsers and evaluators to represent variables, constants,
 * and other named entities that hold numeric values.
 * </p>
 *
 * @param <T> The numeric type that the symbol holds, e.g., {@link Double}, {@link BigDecimal}, or {@link Complex}.
 */
public interface Symbol<T extends Number> extends Nameable {

    /**
     * Retrieves the value of this symbol.
     * <p>
     * This method is used to get the current value associated with the symbol.
     * The value is of a numeric type {@code T}, which could represent any number type,
     * such as a {@code Double}, {@code Integer}, or more complex numeric types.
     * </p>
     *
     * @return The value of this symbol, never {@code null}.
     */
    @NotNull T getValue();
}
