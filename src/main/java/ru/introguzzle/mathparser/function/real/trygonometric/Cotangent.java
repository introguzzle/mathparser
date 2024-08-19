package ru.introguzzle.mathparser.function.real.trygonometric;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;


public class Cotangent extends DoubleFunction {
    public Cotangent() {
        super("cot", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return 1.0 / Math.tan(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
