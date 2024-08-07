package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexSine extends ComplexFunction {
    public ComplexSine() {
        super("sin", 1);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return arguments.getFirst().sin();
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
