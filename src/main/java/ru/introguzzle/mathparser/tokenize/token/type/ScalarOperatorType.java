package ru.introguzzle.mathparser.tokenize.token.type;

import ru.introguzzle.mathparser.operator.Operator;

public interface ScalarOperatorType extends ScalarType, Operator<Double> {
    int BINARY = 2;
    int UNARY = 1;

    @Override
    default Category getCategory() {
        return Category.OPERATOR;
    }
}
