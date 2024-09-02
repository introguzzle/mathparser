package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a mutable symbol in a mathematical expression.
 * <p>
 * This class implements the {@link Symbol} interface and provides a structure
 * for symbols that have a modifiable value. It is used to represent variables or other
 * entities whose values can change during the computation process.
 * </p>
 *
 * @param <N> The numeric type of the value that the symbol holds, e.g., {@link Double}, {@link Integer}.
 * @see Variable
 * @see Coefficient
 * @see LambdaArgument
 */
public abstract class MutableSymbol<N extends Number> implements Symbol<N>, Serializable {

    @Serial
    private static final long serialVersionUID = 282184201759219L;

    /**
     * The name of the symbol. This is a unique identifier used to reference the symbol.
     */
    private final String name;

    /**
     * The mutable value associated with this symbol. This value can be changed.
     */
    private N value;

    /**
     * Constructs a MutableSymbol with the specified name and initial value.
     *
     * @param name  The name of the symbol.
     * @param value The initial value of the symbol.
     */
    public MutableSymbol(String name, N value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull N getValue() {
        return value;
    }

    /**
     * Sets a new value for this symbol.
     *
     * @param value The new value to set.
     * @return The current instance of {@code MutableSymbol}, allowing for method chaining.
     */
    public MutableSymbol<N> setValue(N value) {
        this.value = value;
        return this;
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
        MutableSymbol<?> mutableSymbol = (MutableSymbol<?>) o;
        return Objects.equals(name, mutableSymbol.name);
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
