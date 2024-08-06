package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Sine extends DoubleFunction {

    public Sine() {
        super("sin", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.sin(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
