package ru.function;

import java.util.List;

public class ArcSine extends AbstractFunction {

    public ArcSine() {
        super("arcsin", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.asin(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
