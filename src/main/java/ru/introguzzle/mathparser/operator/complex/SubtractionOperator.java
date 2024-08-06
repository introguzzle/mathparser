package ru.introguzzle.mathparser.operator.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class SubtractionOperator implements ComplexBinaryOperator {
    @Override
    public Complex apply(List<Complex> complexes) {
        return complexes.getFirst().subtract(complexes.getLast());
    }

    @Override
    public @NotNull String getName() {
        return "-";
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public int getPriority() {
        return Priorities.ADDITION_PRIORITY;
    }
}
