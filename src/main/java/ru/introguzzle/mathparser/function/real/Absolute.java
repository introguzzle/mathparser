package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Absolute extends DoubleFunction {

    public Absolute() {
        super("abs", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.abs(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
