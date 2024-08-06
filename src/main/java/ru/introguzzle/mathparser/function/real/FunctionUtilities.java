package ru.introguzzle.mathparser.function.real;

import ru.introguzzle.mathparser.function.Function;

public class FunctionUtilities {
    public static String createExceptionMessage(int given, Function<?> function) {
        int expected = function.getRequiredArguments();

        String givenArgs = given == 1 ? "argument" : "arguments";
        String expectedArgs = expected == 1 || expected == -1 ? "argument" : "arguments";

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
