package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexCosine extends ComplexFunction {
    public ComplexCosine() {
        super("cos", 1);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return arguments.getFirst().cos();
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
