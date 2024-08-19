package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * The MultiNameable interface extends the Nameable interface to provide support
 * for objects that have multiple alternative names or aliases, in addition to a primary name.
 * The primary name is returned by the {@link #getName()} method, while alternative names are
 * returned by the {@link #getAlternativeNames()} method.
 */
public interface MultiNameable extends Nameable {

    /**
     * Returns a set of alternative names (aliases) for this object. The primary name, as returned
     * by {@link #getName()}, is not included in this set.
     *
     * @return a set of alternative names for this object.
     */
    @NotNull
    Set<String> getAlternativeNames();

    /**
     * Checks if the provided character sequence matches either the primary name or any of the
     * alternative names for this object.
     *
     * @param sequence the character sequence to compare with.
     * @return true if the sequence matches the primary name or any of the alternative names, false otherwise.
     */
    @Override
    default boolean nameEquals(CharSequence sequence) {
        for (String name : getAlternativeNames()) {
            if (name.contentEquals(sequence)) {
                return true;
            }
        }

        return Nameable.super.nameEquals(sequence);
    }
}
