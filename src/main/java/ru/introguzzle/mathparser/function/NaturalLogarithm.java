package ru.introguzzle.mathparser.function;

import java.util.List;

public class NaturalLogarithm extends AbstractFunction {

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
