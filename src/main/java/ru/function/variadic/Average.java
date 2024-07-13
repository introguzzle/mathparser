package ru.function.variadic;

import ru.function.AbstractFunction;

import java.util.List;

public class Average extends AbstractFunction {
    public Average() {
        super("avg", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return arguments.stream().reduce(Double::sum).orElse(0.0);
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}
