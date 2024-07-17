package ru.introguzzle.function;

import java.util.List;

public class SquareRoot extends AbstractFunction {

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
