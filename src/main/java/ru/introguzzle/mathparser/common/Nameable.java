package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Interface for objects that can be identified by a name.
 * This interface is central to tokenizing operations.
 * Implementations of this interface include variables, constants, functions, and operators.
 * Provides default implementations for common name-based operations.
 */
public interface Nameable extends Cloneable {

    /**
     * Gets the name of this object.
     *
     * @return the name of this object.
     */
    @NotNull String getName();

    /**
     * Provides a default description for this object.
     * The description is the simple class name of the implementing class.
     *
     * @return a string description of this object.
     */
    @NotNull default String describe() {
        return getClass().getSimpleName();
    }

    /**
     * Checks if the name of this object equals the provided character sequence.
     *
     * @param sequence the character sequence to compare with.
     * @return true if the names are equal, false otherwise.
     */
    default boolean nameEquals(CharSequence sequence) {
        return match(sequence).test(this);
    }

    /**
     * Creates a predicate that matches Nameable objects with the specified character sequence.
     *
     * @param sequence the character sequence to match.
     * @return a predicate that matches Nameable objects with the specified name.
     */
    static Predicate<Nameable> match(CharSequence sequence) {
        return s -> s.getName().contentEquals(sequence);
    }

    /**
     * Creates a predicate that matches strings with the name of the specified Nameable object.
     *
     * @param nameable the Nameable object to match.
     * @return a predicate that matches strings with the specified name.
     */
    static Predicate<String> match(Nameable nameable) {
        return s -> s.contentEquals(nameable.getName());
    }

    /**
     * Converts a collection of Nameable objects into a map with names as keys.
     *
     * @param nameables the collection of Nameable objects.
     * @return a map with names as keys and Nameable objects as values.
     */
    static Map<String, Nameable> toMap(Collection<? extends Nameable> nameables) {
        return nameables.stream().collect(Collectors.toMap(Nameable::getName, n -> n));
    }

    /**
     * Gets a token representing this Nameable object with the specified offset.
     *
     * @param offset the offset of the token.
     * @return a token representing this Nameable object.
     */
    default Token getToken(int offset) {
        return SimpleToken.of(type(), getName(), offset);
    }

    /**
     * Gets the type of this Nameable object.
     *
     * @return the type of this Nameable object.
     */
    @NotNull Type type();

    /**
     * Checks if this object is equal to another object.
     *
     * @param o the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    boolean equals(Object o);
}
