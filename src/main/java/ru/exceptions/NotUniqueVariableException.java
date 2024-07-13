package ru.exceptions;

import ru.variable.Variable;

public class NotUniqueVariableException extends RuntimeException {

    public NotUniqueVariableException(String message) {
        super(message);
    }

    public NotUniqueVariableException(Variable variable) {
        super("Variable " + variable.getRepresentation() + " is already present");
    }
}
