package ru.introguzzle.mathparser.function.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;

import java.util.List;

public class ComplexPower extends ComplexFunction {
    public ComplexPower() {
        super("pow", 2);
    }

    @Override
    public @NotNull Complex apply(List<Complex> arguments) {
        return arguments.getFirst().pow(arguments.getLast());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
