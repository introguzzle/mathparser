package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Floor extends DoubleFunction {
    public Floor() {
        super("floor", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.floor(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
