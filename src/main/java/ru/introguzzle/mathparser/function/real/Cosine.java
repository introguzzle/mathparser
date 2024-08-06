package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Cosine extends DoubleFunction {

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

