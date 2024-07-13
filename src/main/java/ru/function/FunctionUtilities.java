package ru.function;

public class FunctionUtilities {
    public static String message(int given, Function function) {
        StringBuilder sb = new StringBuilder();

        int expected = function.getRequiredArguments();

        String givenArgs = given == 1 ? "argument" : "arguments";
        String expectedArgs = expected == 1 || expected == -1 ? "argument" : "arguments";

        String args = "";

        if (expected == -1)
            args = "(args...)";
        if (expected == 0)
            args = "()";
        if (expected == 1)
            args = "(arg)";
        if (expected == 2)
            args = "(arg, arg)";

        sb.append("Found ")
                .append(given)
                .append(" ")
                .append(givenArgs)
                .append(", expected ")
                .append(expected == -1 ? "at least 1" : expected)
                .append(" ")
                .append(expectedArgs)
                .append(" in function ")
                .append(function.getName())
                .append(args);

        return sb.toString();
    }
}
