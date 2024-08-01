package ru.introguzzle.mathparser.operator;

import ru.introguzzle.mathparser.tokenize.token.type.ScalarType;

public interface ScalarOperatorType extends ScalarType, Operator<Double> {
    int BINARY = 2;
    int UNARY = 1;

    @Override
    default Category getCategory() {
        return Category.OPERATOR;
    }
}
