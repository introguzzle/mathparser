package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import java.util.List;

public class Sum extends DoubleFunction {
    public Sum() {
        super("sum", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
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
