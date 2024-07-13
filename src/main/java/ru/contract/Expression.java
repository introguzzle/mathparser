package ru.contract;

import java.util.List;

public interface Expression extends Iterable<Character> {
    int getLength();
    int getPosition();
    String getString();

    char current();

    List<? extends Symbol> getSymbols();

    boolean isCurrentDigit();

    boolean isCurrentLetter();
    boolean isNextDigit();
    boolean isNextLetter();

    char next();

    boolean hasNext();

    char peekNext();

    Expression reset();
}
