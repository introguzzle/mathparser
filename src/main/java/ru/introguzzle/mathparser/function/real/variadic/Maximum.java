package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;
import java.util.Set;

public class Maximum extends DoubleFunction implements MultiNameable {
    public Maximum() {
        super("max", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
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

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("maximum");
    }
}
