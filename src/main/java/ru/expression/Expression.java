package ru.expression;

import ru.symbol.Symbol;

import java.util.List;

public interface Expression extends Iterable<Character>, Comparable<Expression> {
    int getLength();
    int getPosition();
    String getString();

    char current();

    boolean isCurrentDigit();

    boolean isCurrentLetter();
    boolean isNextDigit();
    boolean isNextLetter();

    char next();

    boolean hasNext();

    char peekNext();

    Expression reset();
}
