package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Exponent extends DoubleFunction {
    public Exponent() {
        super("exp", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.exp(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
