package ru.introguzzle.mathparser.function.variadic;

import ru.introguzzle.mathparser.function.AbstractFunction;

import java.util.List;

public class Maximum extends AbstractFunction {
    public Maximum() {
        super("max", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        double max = arguments.getFirst();

        for (Double argument: arguments) {
            max = Math.max(argument, max);
        }

        return max;
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}
