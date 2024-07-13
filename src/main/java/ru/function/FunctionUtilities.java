package ru.function;

public class FunctionUtilities {
    public static String message(int given, Function function) {
        int expected = function.getRequiredArguments();

        String givenArgs = given == 1 ? "argument" : "arguments";
        String expectedArgs = expected == 1 || expected == -1 ? "argument" : "arguments";

        String args = function.isVariadic() ? "(args...)" : switch (expected) {
            case 0 -> "()";
            case 1 -> "(arg)";
            case 2 -> "(arg, arg)";
            default -> "";
        };

        return String.format("Found %d %s, expected %s %s in function %s%s",
                given,
                givenArgs,
                expected == -1 ? "at least 1" : String.valueOf(expected),
                expectedArgs,
                function.getName(),
                args);
    }
}
