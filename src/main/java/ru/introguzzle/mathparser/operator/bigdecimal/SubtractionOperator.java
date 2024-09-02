package ru.introguzzle.mathparser.operator.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Priorities;

import java.math.BigDecimal;
import java.util.List;

public class SubtractionOperator implements BigDecimalOperator {
    @Override
    public BigDecimal apply(List<BigDecimal> operands) {
        return operands.getFirst().subtract(operands.getLast());
    }

    @Override
    public int getRequiredOperands() {
        return BINARY;
    }

    @Override
    public @NotNull String getName() {
        return "-";
    }

    @Override
    public @NotNull Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public int getPriority() {
        return Priorities.ADDITION_PRIORITY;
    }
}
