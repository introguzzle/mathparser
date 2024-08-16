package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Gamma extends DoubleFunction {
    private static final double[] COEFFICIENTS = {
            1.000000000190015,
            76.18009172947146,
            -86.50532032941677,
            24.01409824083091,
            -1.231739572450155,
            0.001208650973866179,
            -0.000005395239384953
    };

    public Gamma() {
        super("Г", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        return gamma(arguments.getFirst());
    }

    private double gamma(double x) {
        double tmp = x + 5.5;
        tmp -= (x + 0.5) * Math.log(tmp);
        double sum = COEFFICIENTS[0];
        for (int i = 1; i < COEFFICIENTS.length; i++) {
            sum += COEFFICIENTS[i] / (x + i);
        }

        return Math.exp(-tmp + Math.log(2.5066282746310007 * sum / x));
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
