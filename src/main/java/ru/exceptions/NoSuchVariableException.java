package ru.exceptions;

import ru.variable.Variable;

import java.util.Set;

public class NoSuchVariableException extends RuntimeException {
    public NoSuchVariableException(String symbol, Set<String> names) {
        super("No such variable with name: " + symbol + ". Current names: " + names);
    }
}
