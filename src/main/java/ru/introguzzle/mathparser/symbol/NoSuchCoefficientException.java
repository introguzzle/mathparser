package ru.introguzzle.mathparser.symbol;

import java.util.Set;

public class NoSuchCoefficientException extends MutableSymbolListException {
    public NoSuchCoefficientException(String name, Set<String> names) {
        super("No such variable with name: " + name + ". Current names: " + names);
    }
}
