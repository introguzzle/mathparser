package ru.introguzzle.mathparser.operator.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Priorities;

import java.math.BigDecimal;
import java.util.List;

public class AdditionOperator implements BigDecimalOperator {
    @Override
    public int getRequiredOperands() {
        return BINARY;
    }

    @Override
    public BigDecimal apply(List<BigDecimal> operands) {
        return operands.getFirst().add(operands.getLast(), MATH_CONTEXT);
    }

    @Override
    public @NotNull String getName() {
        return "+";
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
