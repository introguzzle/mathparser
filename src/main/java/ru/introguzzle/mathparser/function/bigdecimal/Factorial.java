package ru.introguzzle.mathparser.function.bigdecimal;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.LongStream;

public class Factorial extends BigDecimalFunction {
    public Factorial() {
        super("factorial", 1);
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull BigDecimal evaluate(List<BigDecimal> arguments) {
        BigDecimal n = arguments.getFirst();

        if (n.longValueExact() < 2) {
            return BigDecimal.ONE;
        }

        return LongStream
                .rangeClosed(2, n.longValueExact())
                .parallel()
                .mapToObj(BigDecimal::valueOf)
                .reduce(BigDecimal::multiply)
                .orElse(BigDecimal.ZERO);
    }
}
