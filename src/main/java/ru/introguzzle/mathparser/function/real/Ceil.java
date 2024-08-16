package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Ceil extends DoubleFunction {
    public Ceil() {
        super("ceil", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return Math.ceil(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
