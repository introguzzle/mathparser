package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexNaturalLogarithm extends ComplexFunction {
    public ComplexNaturalLogarithm() {
        super("ln", 1);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return arguments.getFirst().ln();
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
