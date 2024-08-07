package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import java.util.List;

public class Maximum extends DoubleFunction {
    public Maximum() {
        super("max", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
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
