package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cosine extends DoubleFunction {

    public Cosine() {
        super("cos", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.cos(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}

