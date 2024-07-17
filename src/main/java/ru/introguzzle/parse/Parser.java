package ru.introguzzle.parse;

import ru.introguzzle.common.Context;
import ru.introguzzle.common.MathSyntaxException;
import ru.introguzzle.expression.Expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public interface Parser<T extends Number> {
    T parse(Expression expression) throws MathSyntaxException;
    T parse(Expression expression, Context context) throws MathSyntaxException;

    BigDecimal parseBigDecimal(Expression expression) throws MathSyntaxException;
    BigDecimal parseBigDecimal(Expression expression, Context context) throws MathSyntaxException;

    BigInteger parseBigInteger(Expression expression) throws MathSyntaxException;

    BigInteger parseBigInteger(Expression expression, Context context) throws MathSyntaxException;
    Optional<T> parseOptional(Expression expression);
    Optional<T> parseOptional(Expression expression, Context context);
}
