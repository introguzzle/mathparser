package ru.introguzzle.mathparser.operator.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Priorities;

import java.math.BigDecimal;
import java.util.List;

public class ExponentOperator implements BigDecimalOperator {
    @Override
    public BigDecimal apply(List<BigDecimal> operands) {
        return operands.getFirst().pow(operands.getLast().intValueExact(), MATH_CONTEXT);
    }

    @Override
    public int getRequiredOperands() {
        return BINARY;
    }

    @Override
    public @NotNull String getName() {
        return "**";
    }

    @Override
    public @NotNull Association getAssociation() {
        return Association.RIGHT;
    }

    @Override
    public int getPriority() {
        return Priorities.EXPONENT_PRIORITY;
    }
}
