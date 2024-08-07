package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

public class Factorial extends DoubleFunction {

    public Factorial() {
        super("factorial", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
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
}
