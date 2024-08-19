package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;
import java.util.Set;

public class GCD extends DoubleFunction implements MultiNameable {
    public GCD() {
        super("gcd", 2);
    }

    @Override
    public boolean isVariadic() {
        return true;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return Math.abs(a);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        long gcd = arguments.getFirst().longValue();
        for (int i = 1; i < arguments.size(); i++) {
            gcd = gcd(gcd, arguments.get(i).longValue());
        }

        return (double) gcd;
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("НОД", "GCD");
    }
}
