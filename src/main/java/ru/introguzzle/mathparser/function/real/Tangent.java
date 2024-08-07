package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Tangent extends DoubleFunction {

    public Tangent() {
        super("tan", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.tan(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
