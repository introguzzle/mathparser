package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;

import java.util.List;
import java.util.Set;

public class Sign extends DoubleFunction implements MultiNameable {
    public Sign() {
        super("sgn", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.signum(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("sign");
    }
}
