package ru.introguzzle.mathparser.function.real.trygonometric;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;

public class Cosine extends DoubleFunction {

    public Cosine() {
        super("cos", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.cos(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}

