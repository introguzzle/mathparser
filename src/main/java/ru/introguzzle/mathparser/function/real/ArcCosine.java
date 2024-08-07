package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArcCosine extends DoubleFunction {

    public ArcCosine() {
        super("arccos", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.acos(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
