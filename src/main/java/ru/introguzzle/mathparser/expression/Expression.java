package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.NotNull;

public interface Expression extends Iterable<Character>, Comparable<Expression> {
    static Expression of(String string) {
        return new MathExpression(string);
    }

    int getLength();
    String getString();

    @NotNull ExpressionIterator iterator();
}
