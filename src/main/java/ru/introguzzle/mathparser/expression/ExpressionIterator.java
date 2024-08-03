package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

public class ExpressionIterator implements Iterator<Character> {
    private final Expression expression;
    private int cursor = 0;

    public int getCursor() {
        return cursor;
    }

    public ExpressionIterator(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean hasNext() {
        return cursor < expression.getLength();
    }

    @Override
    public Character next() {
        return at(cursor++);
    }

    public Character peekNext() {
        if (!hasNext()) {
            return null;
        }

        return at(cursor + 1);
    }

    public Character at(int index) {
        if (index < expression.getLength()) {
            return expression.getString().charAt(index);
        }

        return null;
    }

    private static final String SPECIAL_CHARS = "+-/*~!@#$%^&*()\"{}_[]|\\?/<>,.=";
    public boolean isSpecial(@NotNull String specialChars) {
        return specialChars.indexOf(at(cursor)) != -1;
    }

    public boolean isDigit(@NotNull Predicate<Character> digitPredicate) {
        return digitPredicate.test(at(cursor));
    }

    public boolean isLetter(@NotNull Predicate<Character> letterPredicate) {
        return letterPredicate.test(at(cursor));
    }

    public boolean isNextDigit(@NotNull Predicate<Character> digitPredicate) {
        if (!hasNext()) {
            return false;
        }

        return digitPredicate.test(peekNext());
    }

    public boolean isNextLetter(@NotNull Predicate<Character> letterPredicate) {
        if (!hasNext()) {
            return false;
        }

        return letterPredicate.test(peekNext());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public char current() {
        return at(cursor);
    }
}
