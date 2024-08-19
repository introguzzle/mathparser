package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;

import java.util.List;
import java.util.Set;

public class Power extends DoubleFunction implements MultiNameable {
    public Power() {
        super("pow", 2);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.pow(arguments.getFirst(), arguments.getLast());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("power");
    }
}
