package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;

import java.util.List;
import java.util.Set;

public class Gamma extends DoubleFunction implements MultiNameable {
    private static final Factorial F = new Factorial();
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

    private static double gamma(double x) {
        if (x - (int) x == 0.0) {
            return F.evaluate(List.of(x - 1));
        }

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

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("GAMMA", "gamma");
    }
}
