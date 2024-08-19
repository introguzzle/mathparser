package ru.introguzzle.mathparser.function.real.variadic;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;
import ru.introguzzle.mathparser.function.real.DoubleFunction;

import java.util.List;
import java.util.Set;

public class LCM extends DoubleFunction implements MultiNameable {
    public LCM() {
        super("lcm", 2);
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

    private long lcm(long a, long b) {
        return Math.abs(a * (b / gcd(a, b)));
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        long lcm = arguments.getFirst().longValue();
        for (int i = 1; i < arguments.size(); i++) {
            lcm = lcm(lcm, arguments.get(i).longValue());
        }

        return (double) lcm;
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("НОК", "LCM");
    }
}
