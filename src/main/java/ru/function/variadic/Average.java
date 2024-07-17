package ru.function.variadic;

import ru.function.AbstractFunction;

import java.util.List;

public class Average extends AbstractFunction {
    public Average() {
        super("avg", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return arguments
                .stream()
                .reduce(Double::sum)
                .orElseThrow(RuntimeException::new)
                / arguments.size();
    }

    @Override
    public boolean isVariadic() {
        return true;
    }
}
