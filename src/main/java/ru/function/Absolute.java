package ru.function;

import java.util.List;

public class Absolute extends AbstractFunction {

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
