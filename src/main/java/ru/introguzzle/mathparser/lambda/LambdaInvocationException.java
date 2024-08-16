package ru.introguzzle.mathparser.lambda;

import ru.introguzzle.mathparser.common.ExceptionUtilities;

public class LambdaInvocationException extends RuntimeException {
    public LambdaInvocationException(int given, LambdaEvaluator<?> evaluator) {
        super(createExceptionMessage(given, evaluator));
    }

    public static String createExceptionMessage(int given, LambdaEvaluator<?> evaluator) {
        int expected = evaluator.getRequiredArguments();

        String argument = "argument";
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
