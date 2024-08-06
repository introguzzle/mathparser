package ru.introguzzle.mathparser.function.real.variadic;

import ru.introguzzle.mathparser.function.real.DoubleFunction;
import java.util.List;

public class Average extends DoubleFunction {
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
