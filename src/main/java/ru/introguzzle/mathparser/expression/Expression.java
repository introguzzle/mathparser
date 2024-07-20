package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.NotNull;

public interface Expression extends Iterable<Character>, Comparable<Expression> {

    int getLength();
    String getString();

    @NotNull ExpressionIterator iterator();
}
