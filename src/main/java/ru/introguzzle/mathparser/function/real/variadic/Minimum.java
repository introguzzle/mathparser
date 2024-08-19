package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;
import java.util.Set;

public class Minimum extends DoubleFunction implements MultiNameable {
    public Minimum() {
        super("min", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
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

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("minimum");
    }
}
