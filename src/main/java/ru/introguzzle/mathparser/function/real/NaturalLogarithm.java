package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class NaturalLogarithm extends DoubleFunction {

    public NaturalLogarithm() {
        super("ln", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.log(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
