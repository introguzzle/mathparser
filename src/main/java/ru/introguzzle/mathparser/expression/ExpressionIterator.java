package ru.introguzzle.mathparser.expression;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public class ExpressionIterator implements Iterator<Character> {
    private static final char DECIMAL = '.';
    private static final char IMAGINARY_UNIT = 'i';
    private static final char UNDERSCORE = '_';

    private final Expression expression;
    private int cursor = 0;

    public int getCursor() {
        return cursor;
    }

    private static class Char {
        private static boolean isLetter(Character c) {
            if (c == null) {
                return false;
            }

            return c == UNDERSCORE || Character.isLetter(c);
        }

        private static boolean isDigit(Character c) {
            if (c == null) {
                return false;
            }

            return c == DECIMAL || c == IMAGINARY_UNIT || Character.isDigit(c);
        }
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
    public boolean isSpecial(@Nullable String specialChars) {
        return Objects
                .requireNonNullElse(specialChars, SPECIAL_CHARS)
                .indexOf(at(cursor)) != -1;

    }

    public boolean isDigit(Predicate<Character> digitPredicate) {
        return digitPredicate == null
                ? Char.isDigit(at(cursor))
                : digitPredicate.test(at(cursor));
    }

    public boolean isLetter(Predicate<Character> letterPredicate) {
        return letterPredicate == null
                ? Char.isLetter(at(cursor))
                : letterPredicate.test(at(cursor));
    }

    public boolean isNextDigit(Predicate<Character> digitPredicate) {
        if (!hasNext()) {
            return false;
        }

        return digitPredicate == null
                ? Char.isDigit(peekNext())
                : digitPredicate.test(peekNext());
    }

    public boolean isNextLetter(Predicate<Character> letterPredicate) {
        if (!hasNext()) {
            return false;
        }

        return letterPredicate == null
                ? Char.isLetter(peekNext())
                : letterPredicate.test(peekNext());
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
