package ru.introguzzle.mathparser.lambda;

/**
 * This interface is used to define special functions that involve delimited arguments
 * and lambda expressions, such as sum(1, 2, n -> n).
 * <p>
 * For example, in the expression sum(1, 2, n -> n):
 * - The function expects 2 commas to delimit its arguments (i.e., between 1, 2, and n -> n).
 * - The lambda expression starts after the second argument, so the index of the lambda expression is 2.
 * <p>
 * Implementations of this interface should specify the number of required commas
 * and the index at which the lambda expression begins.
 */
public interface Delimitable {
    /**
     * Returns the number of commas required to delimit the arguments before the lambda expression.
     *
     * @return the number of commas required.
     */
    int getRequiredCommas();

    /**
     * Returns the index position at which the lambda expression begins within the argument list.
     *
     * @return the index of the lambda expression.
     */
    int getLambdaGroupIndex();
}
