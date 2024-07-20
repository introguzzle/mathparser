package ru.introguzzle.mathparser.function;

import java.util.List;
import java.util.random.RandomGenerator;

public class Random extends AbstractFunction {
    public Random() {
        super("random", 0);
    }

    @Override
    public Double apply(List<Double> arguments) {
        double v1 = java.util.Random.from(RandomGenerator.getDefault()).nextDouble(-1, 1);
        double v2 = java.util.Random.from(RandomGenerator.getDefault()).nextDouble(-100, 100);
        return v1 * v2;
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
