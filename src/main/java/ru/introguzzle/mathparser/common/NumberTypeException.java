package ru.introguzzle.mathparser.common;

import java.io.Serial;

/**
 * Represents an exception thrown when an invalid number type is encountered.
 * <p>
 * This exception is typically used to indicate errors related to type mismatches in numeric operations,
 * such as attempting to cast a number to an incompatible type.
 * </p>
 */
public final class NumberTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8533732822571178406L;

    /**
     * Constructs a new {@code NumberTypeException} with no detail message.
     * <p>
     * This constructor creates an exception instance that doesn't provide additional context
     * about the cause of the error. It's typically used when the exception is self-explanatory
     * or when further details are unnecessary.
     * </p>
     */
    public NumberTypeException() {

    }

    /**
     * Constructs a new {@code NumberTypeException} with the specified detail message.
     * <p>
     * This constructor allows you to provide a specific error message that explains the context
     * or reason for the exception. The message can be retrieved later using the {@code getMessage()} method.
     * </p>
     *
     * @param message The detail message that explains the reason for the exception.
     */
    public NumberTypeException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code NumberTypeException} with the specified detail message and cause.
     * <p>
     * This constructor allows you to provide both a specific error message and an underlying cause
     * for the exception, such as a {@link ClassCastException}. This can be useful for chaining exceptions
     * and providing more context about the error.
     * </p>
     *
     * @param message The detail message that explains the reason for the exception.
     * @param cause   The underlying cause of the exception, typically a {@link ClassCastException}.
     */
    public NumberTypeException(String message, ClassCastException cause) {
        super(message, cause);
    }
}
