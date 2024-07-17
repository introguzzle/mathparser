package ru.introguzzle.function;

import java.util.List;

public class NaturalRoot extends AbstractFunction {


    public NaturalRoot() {
        super("root", 2);
    }

    @Override
    public Double apply(List<Double> arguments) {
        double n = arguments.getFirst();
        double x = arguments.getLast();

        return ((int) Math.floor(n)) % 2 == 0
                ? Math.pow(x, 1.0 / n)
                : (x >= 0 ? Math.pow(x, 1.0 / n) : -Math.pow(-x, 1.0 / n));
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
