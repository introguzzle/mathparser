package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Tangent extends DoubleFunction {

    public Tangent() {
        super("tan", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.tan(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
