package ru.function.variadic;

import ru.function.AbstractFunction;

import java.util.List;

public class Minimum extends AbstractFunction {
    public Minimum() {
        super("min", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        double min = arguments.getFirst();

        for (Double argument: arguments) {
            min = Math.min(argument, min);
        }

        return min;
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}
