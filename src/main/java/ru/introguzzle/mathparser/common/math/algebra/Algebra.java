package ru.introguzzle.mathparser.common.math.algebra;

public interface Algebra<T extends Number> {
    /**
     * Compares two values.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @param left The left operand.
     * @param right The right operand.
     * @return {@code true} if the left operand is greater than the right operand, {@code false} otherwise.
     */
    int compare(T left, T right) throws UnsupportedAlgebraOperationException;

    /**
     * Returns the value that represents the absence of a value in the parser's context.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @return The absent value.
     * @throws UnsupportedAlgebraOperationException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    T absentValue() throws UnsupportedAlgebraOperationException;

    /**
     * Negates a given value.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @param value The value to negate.
     * @return The negated value.
     * @throws UnsupportedAlgebraOperationException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    T negateValue(T value) throws UnsupportedAlgebraOperationException;

    /**
     * Adds two values.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @param left The left operand.
     * @param right The right operand.
     * @return The sum of the left and right operands.
     * @throws UnsupportedAlgebraOperationException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    T add(T left, T right) throws UnsupportedAlgebraOperationException;
}
