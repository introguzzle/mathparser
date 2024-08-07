package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SquareRoot extends DoubleFunction {

    public SquareRoot() {
        super("sqrt", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.sqrt(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
