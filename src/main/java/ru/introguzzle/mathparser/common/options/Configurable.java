package ru.introguzzle.mathparser.common.options;

/**
 * Interface representing an object that can be configured with specific options.
 * <p>
 * Implementations of this interface are able to store and retrieve configuration
 * options of a certain type.
 * </p>
 *
 * @param <O> The type of options that this object can be configured with.
 */
public interface Configurable<O extends Options> {

    /**
     * Sets the options for this object.
     * <p>
     * This method allows configuring the object with the provided options.
     * </p>
     *
     * @param options The options to set.
     */
    void setOptions(O options);

    /**
     * Retrieves the current options of this object.
     * <p>
     * This method returns the options currently configured for this object.
     * </p>
     *
     * @return The current options of this object.
     */
    O getOptions();
}
