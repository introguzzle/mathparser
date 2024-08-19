package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Factorial extends DoubleFunction implements MultiNameable {

    public Factorial() {
        super("factorial", 1);
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        double d = arguments.getFirst();
        int n = (int) d;

        if (n < 2) {
            return BigInteger.valueOf(1).doubleValue();
        }

        return IntStream
                .rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger::multiply)
                .orElse(new BigInteger("0"))
                .doubleValue();
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("fact");
    }
}
