package ru.introguzzle.mathparser.lambda;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.parse.Parser;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.LambdaType;

import java.util.List;

/**
 * Interface representing a lambda expression evaluator in a mathematical parser.
 * <p>
 * This interface defines the required methods for evaluating lambda expressions,
 * including handling argument validation, lambda function application, and dealing
 * with variadic arguments.
 * </p>
 *
 * @param <T> The numeric type that the evaluator processes, typically a subclass of {@link Number}.
 */
public interface LambdaEvaluator<T extends Number> extends Nameable {

    /**
     * Returns the parser associated with this lambda evaluator.
     *
     * @return The parser used by this evaluator.
     */
    @NotNull Parser<T> getParser();

    /**
     * Returns the type of the lambda evaluator, which is used for tokenization and parsing.
     * <p>
     * By default, this method returns {@link LambdaType#LAMBDA}.
     * </p>
     *
     * @return The type of the lambda evaluator.
     */
    @Override
    default @NotNull LambdaType type() {
        return LambdaType.LAMBDA;
    }

    /**
     * Returns the number of regular arguments required by this lambda evaluator.
     *
     * @return The number of required regular arguments.
     */
    int getRequiredArguments();

    /**
     * Returns the number of lambda-specific arguments required by this evaluator.
     *
     * @return The number of required lambda arguments.
     */
    int getRequiredLambdaArguments();

    /**
     * Indicates whether this lambda evaluator supports a variable number of regular arguments.
     *
     * @return {@code true} if the evaluator supports variadic arguments, {@code false} otherwise.
     */
    boolean isVariadic();

    /**
     * Indicates whether this lambda evaluator supports a variable number of lambda-specific arguments.
     *
     * @return {@code true} if the evaluator supports variadic lambda arguments, {@code false} otherwise.
     */
    boolean isLambdaVariadic();

    /**
     * Evaluates the lambda expression with the provided arguments, lambda arguments, and context.
     *
     * @param arguments       The list of arguments provided to the lambda expression.
     * @param lambdaArguments The list of lambda-specific arguments.
     * @param lambda          The tokens representing the lambda expression.
     * @param context         The context in which the lambda expression is evaluated.
     * @return The result of the evaluation as an instance of {@code T}.
     * @throws SyntaxException If an error occurs during the evaluation of the lambda expression.
     */
    @NotNull T evaluate(List<T> arguments,
                        List<LambdaArgument<T>> lambdaArguments,
                        Tokens lambda,
                        Context<T> context)
            throws SyntaxException;

    /**
     * Applies the lambda evaluator to the provided arguments and context.
     * <p>
     * This method checks that the number of arguments and lambda arguments meets the
     * evaluator's requirements before invoking the evaluation. If the requirements are not met,
     * it throws an appropriate exception.
     * </p>
     *
     * @param arguments       The list of arguments provided to the lambda expression.
     * @param lambdaArguments The list of lambda-specific arguments.
     * @param lambda          The tokens representing the lambda expression.
     * @param context         The context in which the lambda expression is evaluated.
     * @return The result of the evaluation as an instance of {@code T}.
     * @throws SyntaxException If the evaluation fails due to incorrect arguments or context issues.
     */
    @NotNull default T apply(List<T> arguments,
                             List<LambdaArgument<T>> lambdaArguments,
                             Tokens lambda,
                             Context<T> context)
            throws SyntaxException {
        int size = arguments.size();

        // Check if the number of arguments meets the required minimum
        if (size < getRequiredArguments()) {
            throw getInvocationException(Mismatch.DEFAULT, arguments.size());
        }

        // Check if the number of arguments is correct when not variadic
        if (!isVariadic() && size != getRequiredArguments()) {
            throw getInvocationException(Mismatch.DEFAULT, arguments.size());
        }

        int lambdaSize = lambdaArguments.size();

        // Check if the number of lambda arguments meets the required minimum
        if (lambdaSize < getRequiredLambdaArguments()) {
            throw getInvocationException(Mismatch.LAMBDA, arguments.size());
        }

        // Check if the number of lambda arguments is correct when not variadic
        if (!isLambdaVariadic() && size != getRequiredLambdaArguments()) {
            throw getInvocationException(Mismatch.LAMBDA, arguments.size());
        }

        // Remove lambda arguments from the context to prevent conflicts
        for (LambdaArgument<T> lambdaArgument : lambdaArguments) {
            context.removeSymbol(lambdaArgument.getName());
        }

        // Evaluate the lambda expression with the given arguments
        return evaluate(arguments, lambdaArguments, lambda, context);
    }

    /**
     * Creates an exception to be thrown when the invocation of the lambda evaluator fails due to incorrect arguments.
     *
     * @param mismatch The type of argument mismatch (regular or lambda).
     * @param given    The number of arguments provided.
     * @return A RuntimeException indicating the reason for the failure.
     */
    private @NotNull RuntimeException getInvocationException(Mismatch mismatch, int given) {
        return new LambdaInvocationException(mismatch, given, this);
    }

    /**
     * Enumeration to represent the type of argument mismatch.
     */
    enum Mismatch {
        DEFAULT,  // Indicates a mismatch in regular arguments
        LAMBDA    // Indicates a mismatch in lambda-specific arguments
    }
}
