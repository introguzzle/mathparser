package ru.contract;

import ru.exceptions.MathSyntaxException;
import ru.variable.Variables;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public interface Parser<T extends Number> {
    T parse(Expression expression) throws MathSyntaxException;
    T parse(Expression expression, Variables variables) throws MathSyntaxException;

    BigDecimal parseBigDecimal(Expression expression) throws MathSyntaxException;
    BigDecimal parseBigDecimal(Expression expression, Variables variables) throws MathSyntaxException;

    BigInteger parseBigInteger(Expression expression) throws MathSyntaxException;

    BigInteger parseBigInteger(Expression expression, Variables variables) throws MathSyntaxException;
    Optional<T> parseOptional(Expression expression);
    Optional<T> parseOptional(Expression expression, Variables variables);
}
