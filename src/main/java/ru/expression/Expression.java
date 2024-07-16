package ru.expression;

public interface Expression extends Iterable<Character>, Comparable<Expression> {
    int getLength();
    int getCursor();
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

    void setCursor(int cursor);
}
