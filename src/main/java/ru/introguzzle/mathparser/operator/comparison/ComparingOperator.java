package ru.introguzzle.mathparser.operator.comparison;

import ru.introguzzle.mathparser.operator.BinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

public abstract class ComparingOperator implements BinaryOperator {
    @Override
    public int getPriority() {
        return Priorities.COMPARISON_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }
}
