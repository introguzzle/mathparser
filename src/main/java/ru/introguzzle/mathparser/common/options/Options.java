package ru.introguzzle.mathparser.common.options;

import java.util.Arrays;

/**
 * Interface representing configurable options using bitwise flags.
 * <p>
 * This interface provides methods to work with flags, allowing for bitwise operations
 * to check for specific configuration options.
 * </p>
 */
public interface Options {

    /**
     * Returns the current flags of the options.
     * <p>
     * The flags are typically represented as a bitwise integer, where each bit represents
     * a different option that can be enabled or disabled.
     * </p>
     *
     * @return The bitwise flags representing the current options.
     */
    int getFlags();

    /**
     * Checks if a specific flag is enabled.
     * <p>
     * This method performs a bitwise AND operation between the current flags and the
     * provided flag, and returns true if the flag is set.
     * </p>
     *
     * @param flag The flag to check.
     * @return {@code true} if the specified flag is enabled, {@code false} otherwise.
     */
    default boolean match(int flag) {
        return (getFlags() & flag) == flag;
    }

    /**
     * Checks if all specified flags are enabled.
     * <p>
     * This method checks if all provided flags are set in the current flags.
     * It returns true only if all specified flags are enabled.
     * </p>
     *
     * @param flags The flags to check.
     * @return {@code true} if all specified flags are enabled, {@code false} otherwise.
     */
    default boolean matchAll(int... flags) {
        return Arrays.stream(flags).allMatch(this::match);
    }

    /**
     * Checks if any of the specified flags are enabled.
     * <p>
     * This method checks if any of the provided flags are set in the current flags.
     * It returns true if at least one of the specified flags is enabled.
     * </p>
     *
     * @param flags The flags to check.
     * @return {@code true} if any of the specified flags are enabled, {@code false} otherwise.
     */
    default boolean matchAny(int... flags) {
        return Arrays.stream(flags).anyMatch(this::match);
    }
}
