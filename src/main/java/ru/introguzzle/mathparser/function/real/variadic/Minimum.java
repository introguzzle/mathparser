package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import java.util.List;

public class Minimum extends DoubleFunction {
    public Minimum() {
        super("min", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
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
