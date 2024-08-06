package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class SquareRoot extends DoubleFunction {

    public SquareRoot() {
        super("sqrt", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.sqrt(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
