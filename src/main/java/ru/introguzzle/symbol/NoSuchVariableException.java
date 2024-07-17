package ru.introguzzle.symbol;

import java.util.Set;

public class NoSuchVariableException extends MutableSymbolListException {
    public NoSuchVariableException(String name, Set<String> names) {
        super("No such variable with name: " + name + ". Current names: " + names);
    }
}
