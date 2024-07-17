package ru.introguzzle.function;

import java.util.List;

public class Cosine extends AbstractFunction {

    public Cosine() {
        super("cos", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.cos(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}

