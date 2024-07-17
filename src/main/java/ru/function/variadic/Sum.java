package ru.function.variadic;

import ru.function.AbstractFunction;

import java.util.List;

public class Sum extends AbstractFunction {
    public Sum() {
        super("sum", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return arguments
                .stream()
                .reduce(Double::sum)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}
