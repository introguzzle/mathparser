package ru.introguzzle.mathparser.function.real;

// https://ru.wikipedia.org/wiki/%D0%91%D0%B5%D1%82%D0%B0-%D1%84%D1%83%D0%BD%D0%BA%D1%86%D0%B8%D1%8F

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.MultiNameable;

import java.util.List;
import java.util.Set;

// B(x, y) = Г(x) * Г(y) / Г(x + y)
public class Beta extends DoubleFunction implements MultiNameable {
    private static final Gamma G = new Gamma();
    private static double eval(Double argument) {
        return G.evaluate(List.of(argument));
    }

    public Beta() {
        super("B", 2);
    }

    @Override
    public boolean isVariadic() {
        return false;
    }

    @Override
    public @NotNull Double evaluate(List<Double> arguments) {
        double x = arguments.get(0);
        double y = arguments.get(1);

        return eval(x) * eval(y) / eval(x + y);
    }

    @Override
    public @NotNull Set<String> getAlternativeNames() {
        return Set.of("Beta", "BETA");
    }
}
