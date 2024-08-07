package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexAbsolute extends ComplexFunction {
    public ComplexAbsolute() {
        super("abs", 1);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return new Complex(arguments.getFirst().magnitude());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
