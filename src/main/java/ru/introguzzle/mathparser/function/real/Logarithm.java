package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Logarithm extends DoubleFunction {

    public Logarithm() {
        super("log", 2);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.log(arguments.get(1)) / Math.log(arguments.get(0));
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
