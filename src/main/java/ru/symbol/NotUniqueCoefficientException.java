package ru.symbol;

import org.jetbrains.annotations.NotNull;

public class NotUniqueCoefficientException extends MutableSymbolListException {
    public NotUniqueCoefficientException(@NotNull Coefficient coefficient) {
        super("Variable " + coefficient.getName() + " is already present");
    }
}
