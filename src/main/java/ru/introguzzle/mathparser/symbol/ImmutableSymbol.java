package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.constant.complex.ComplexConstant;
import ru.introguzzle.mathparser.constant.bigdecimal.BigDecimalConstant;

/**
 * Represents an immutable symbol in a mathematical expression.
 * <p>
 * This class implements the {@link Symbol} interface and provides a basic structure
 * for symbols with a fixed value and name. It is used to represent constants or other named
 * entities whose values do not change.
 * </p>
 *
 * @param <T> The numeric type of the value that the symbol holds, e.g., {@link Double}, {@link Integer}.
 * @see DoubleConstant
 * @see ComplexConstant
 * @see BigDecimalConstant
 */
public abstract class ImmutableSymbol<T extends Number> implements
        Symbol<T>,
        Serializable {

    @Serial
    private static final long serialVersionUID = 1678692611467409873L;

    /**
     * The name of the symbol. This is a unique identifier used to reference the symbol.
     */
    private final String name;

    /**
     * The immutable value associated with this symbol. Once set, this value cannot change.
     */
    private final T value;

    /**
     * Constructs an ImmutableSymbol with the specified name and value.
     *
     * @param name  The name of the symbol.
     * @param value The value of the symbol.
     */
    public ImmutableSymbol(String name, T value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull T getValue() {
        return value;
    }

    /**
     * Returns a string representation of the symbol.
     * The representation includes the type of the symbol, its name, and its value.
     *
     * @return A string representation of the symbol.
     */
    @Override
    public String toString() {
        return describe() + "{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    /**
     * Compares this symbol to the specified object for equality.
     * Two symbols are considered equal if they have the same name.
     *
     * @param o The object to compare with.
     * @return {@code true} if the symbols are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableSymbol<?> immutableSymbol = (ImmutableSymbol<?>) o;
        return Objects.equals(name, immutableSymbol.name);
    }

    /**
     * Returns a hash code for the symbol based on its name and value.
     *
     * @return A hash code for the symbol.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
