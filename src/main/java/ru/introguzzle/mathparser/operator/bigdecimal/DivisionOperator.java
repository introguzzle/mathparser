package ru.introguzzle.mathparser.operator.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Priorities;

import java.math.BigDecimal;
import java.util.List;

public class DivisionOperator implements BigDecimalOperator {
    @Override
    public BigDecimal apply(List<BigDecimal> operands) {
        return operands.getFirst().divide(operands.getLast(), MATH_CONTEXT);
    }

    @Override
    public int getRequiredOperands() {
        return BINARY;
    }

    @Override
    public @NotNull String getName() {
        return "/";
    }

    @Override
    public @NotNull Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public int getPriority() {
        return Priorities.MULTIPLICATION_PRIORITY;
    }
}
