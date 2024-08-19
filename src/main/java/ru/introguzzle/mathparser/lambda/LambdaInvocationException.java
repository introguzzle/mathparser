package ru.introguzzle.mathparser.lambda;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.ExceptionUtilities;

import java.io.Serial;

public class LambdaInvocationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1011156486436682184L;

    public LambdaInvocationException(@NotNull String message) {
        super(message);
    }

    public LambdaInvocationException(@NotNull LambdaEvaluator.Mismatch mismatch,
                                     int given,
                                     @NotNull LambdaEvaluator<?> evaluator) {
        super(createExceptionMessage(mismatch, given, evaluator));
    }

    static String createExceptionMessage(@NotNull LambdaEvaluator.Mismatch mismatch,
                                         int given,
                                         @NotNull LambdaEvaluator<?> evaluator) {
        int expected = evaluator.getRequiredArguments();

        String argument = mismatch == LambdaEvaluator.Mismatch.DEFAULT
                ? "argument"
                : "lambda argument";

        String givenArgs = ExceptionUtilities.pluralize(argument, given);
        String expectedArgs = ExceptionUtilities.pluralize(argument, expected);

        return String.format("Found %d %s, expected %s %s in function %s",
                given,
                givenArgs,
                evaluator.isVariadic()
                        ? String.format("at least %d", expected)
                        : String.valueOf(expected),
                expectedArgs,
                evaluator.getName()
        );
    }
}
