package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Hypot extends DoubleFunction {
    public Hypot() {
        super("hypot", 2);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.hypot(arguments.get(0), arguments.get(1));
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
