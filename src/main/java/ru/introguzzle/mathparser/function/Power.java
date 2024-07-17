package ru.introguzzle.mathparser.function;

import java.util.List;

public class Power extends AbstractFunction {
    public Power() {
        super("pow", 2);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.pow(arguments.getFirst(), arguments.getLast());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
