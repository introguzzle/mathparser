package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;
import java.util.Set;

public class ComplexMagnitude extends ComplexFunction implements MultiNameable {
    public ComplexMagnitude() {
        super("abs", 1);
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull Complex evaluate(List<Complex> arguments) {
        return Complex.of(arguments.getFirst().magnitude());
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("magnitude");
    }
}
