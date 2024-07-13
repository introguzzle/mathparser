package ru.function;

import java.util.List;

public class ArcCosine extends AbstractFunction {

    public ArcCosine() {
        super("arccos", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.acos(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
