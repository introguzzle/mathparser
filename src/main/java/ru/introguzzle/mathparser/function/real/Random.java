package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.random.RandomGenerator;

public class Random extends DoubleFunction {
    public Random() {
        super("random", 0);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        double v1 = java.util.Random.from(RandomGenerator.getDefault()).nextDouble(-1, 1);
        double v2 = java.util.Random.from(RandomGenerator.getDefault()).nextDouble(-100, 100);
        return v1 * v2;
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
