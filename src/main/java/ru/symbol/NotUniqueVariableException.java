package ru.symbol;

import ru.symbol.Variable;

public class NotUniqueVariableException extends RuntimeException {

    public NotUniqueVariableException(String message) {
        super(message);
    }

    public NotUniqueVariableException(Variable variable) {
        super("Variable " + variable.getName() + " is already present");
    }
}