package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Power extends DoubleFunction {
    public Power() {
        super("pow", 2);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.pow(arguments.getFirst(), arguments.getLast());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
