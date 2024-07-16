package ru.tokenize;


public class VariableMismatchException extends RuntimeException {
    public VariableMismatchException(int given, int actual) {
        super(createMessage(given, actual));
    }

    private static String createMessage(int given, int actual) {
        String g = given == 1 ? "argument" : "arguments";
        String a = actual == 1 ? "argument" : "arguments";
        return String.format("Given %d %s, but expression actually has %d %s", given, g, actual, a);
    }
}
