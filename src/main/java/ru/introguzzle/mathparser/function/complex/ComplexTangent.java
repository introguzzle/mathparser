package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexTangent extends ComplexFunction {
    public ComplexTangent() {
        super("tan", 1);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return arguments.getFirst().tan();
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
