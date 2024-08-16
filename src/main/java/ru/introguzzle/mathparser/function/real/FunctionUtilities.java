package ru.introguzzle.mathparser.function.real;

import ru.introguzzle.mathparser.common.ExceptionUtilities;
import ru.introguzzle.mathparser.function.Function;

public final class FunctionUtilities {
    public static String createExceptionMessage(int given, Function<?> function) {
        int expected = function.getRequiredArguments();

        String argument = "argument";
        String givenArgs = ExceptionUtilities.pluralize(argument, given);
        String expectedArgs = ExceptionUtilities.pluralize(argument, expected);

        return String.format("Found %d %s, expected %s %s in function %s",
                given,
                givenArgs,
                function.isVariadic()
                        ? String.format("at least %d", expected)
                        : String.valueOf(expected),
                expectedArgs,
                function.getName()
        );
    }
}
