package ru.introguzzle.function;

import java.util.List;

public class Logarithm extends AbstractFunction {

    public Logarithm() {
        super("log", 2);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.log(arguments.get(1)) / Math.log(arguments.get(0));
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
